class WordleSolver(allWords: Set<String>) {

    private var candidateSet: Set<String> = HashSet(allWords)
    val permanentPredicates = mutableSetOf<WordlePredicate>()

////    fun determineExpectedValue(guess: String): Double {
////
////    }
//
//    private fun prepareAllPossibilities(guess: String, position: Int): MutableList<MutableList<out WordlePredicate>> {
//
//        if(guess.isEmpty()){
//            return mutableListOf()
//        }
//
//        val possibleConstraints = mutableListOf(mutableListOf(NotInWordPredicate(guess[0])), mutableListOf(PositionalPredicate(guess[0], position)), mutableListOf(AnyPositionPredicate(guess[0])))
//
//        possibleConstraints[0].addAll(prepareAllPossibilities(guess.drop(1), position + 1))
//        possibleConstraints[1].addAll(prepareAllPossibilities(guess.drop(1), position + 1))
//        possibleConstraints[2].addAll(prepareAllPossibilities(guess.drop(1), position + 1))
//
//
//
//        return possibleConstraints
//
//    }

    fun chooseWord(): String? {
        //println(prepareAllPossibilities("flops", 0))

        var bestWord: String? = null
        var bestWordReduction = 0

        candidateSet.forEach { possibleWord ->
            val predicates = mutableListOf<WordlePredicate>()

            possibleWord.forEach { c ->
                predicates.add(AnyPositionPredicate(c))
            }

            val candidateSetWithPredicatesApplied = candidateSet.filterNot { w -> predicates.any { it.applyPredicate(w) } }

            val sizeReduction = candidateSet.size - candidateSetWithPredicatesApplied.size

            if(sizeReduction > bestWordReduction){
                bestWord = possibleWord
                bestWordReduction = sizeReduction
            }

        }

        println("Best word $bestWord would remove $bestWordReduction possibilities")

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