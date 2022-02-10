

class WordleSolver(private val allWords: Set<String>) {
    private var candidateSet: Set<String> = HashSet(allWords)
    val permanentPredicates = mutableSetOf<WordlePredicate>()

    private fun prepareAllPossibilities(string: String): List<List<WordlePredicate>> {
        val allPossibilityBuckets = mutableListOf<List<WordlePredicate>>()

        string.forEachIndexed { index, c ->
            allPossibilityBuckets.add(allPossibilitiesForChar(c, index))
        }

        val allPossiblePaths = mutableListOf<List<WordlePredicate>>()

        return path(allPossibilityBuckets, 0, mutableListOf())

    }

    private fun path(allPossibilityBuckets: List<List<WordlePredicate>>, position: Int, pathToGetHere: List<WordlePredicate>): List<List<WordlePredicate>> {
        if(position == 4){
            return mutableListOf(pathToGetHere.plus(allPossibilityBuckets[4][0]), pathToGetHere.plus(allPossibilityBuckets[4][1]), pathToGetHere.plus(allPossibilityBuckets[4][2]))
        }
        else {
            return path(allPossibilityBuckets, position + 1, pathToGetHere.plus(allPossibilityBuckets[position][0]))
                .plus(path(allPossibilityBuckets, position + 1, pathToGetHere.plus(allPossibilityBuckets[position][1])))
                .plus(path(allPossibilityBuckets, position + 1, pathToGetHere.plus(allPossibilityBuckets[position][2])))
        }
    }



    private fun allPossibilitiesForChar(c: Char, position: Int): List<WordlePredicate> {
        return listOf(NotInPositionPredicate(c, position), NotInWordPredicate(c), PositionalPredicate(c, position));
    }

    fun chooseWordAdvanced(): String? {
        var bestWord: String? = null
        var bestWordReduction = 0.0

        if(candidateSet.size == 1){
            return candidateSet.first()
        }

        allWords.stream().parallel().forEach { possibleWord ->
            val possiblePredicates = prepareAllPossibilities(possibleWord).map { l -> l.plus(permanentPredicates) }

            val bestImpactForWord = possiblePredicates.map { predicates ->
                val candidateSetWithPredicatesApplied = candidateSet.filter { w -> predicates.all { it.applyPredicate(w) } }
                val sizeReduction = candidateSet.size - candidateSetWithPredicatesApplied.size

                val probabilityOfOccurence: Double = candidateSet.count { w -> predicates.all { it.applyPredicate(w) } }.toDouble() / candidateSet.size

                probabilityOfOccurence * sizeReduction

            }.average()


            if(bestImpactForWord != 0.0) {
                println("Best impact for word $possibleWord is $bestImpactForWord")
            }

            if(bestImpactForWord > bestWordReduction){
                bestWord = possibleWord
                bestWordReduction = bestImpactForWord
            }

        }

        return bestWord
    }

    fun addPredicates(newPredicates: List<WordlePredicate>){
        println("Adding predicates $newPredicates")

        permanentPredicates.addAll(newPredicates)

        val notInWordPreds: List<WordlePredicate> = newPredicates.filterIsInstance<NotInWordPredicate>()

        val toRemove = notInWordPreds.filter { notInWordPred ->
            permanentPredicates.filter { it !is NotInWordPredicate }.any { notInWordPred.c == it.c }
        }

        permanentPredicates.removeAll(toRemove.toSet())

        candidateSet = candidateSet.filter { w -> permanentPredicates.all { it.applyPredicate(w) } }.toSet()

        println("New candidate set is ${candidateSet.size} elements")

        println(candidateSet)
        println(permanentPredicates)
    }



}