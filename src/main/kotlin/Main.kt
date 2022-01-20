
fun main() {
    val words = { }.javaClass.classLoader.getResource("wordle.txt").readText().split("\r\n")

    val solver = WordleSolver(words.toSet())

    while (true){
        val lastWord = solver.chooseWord()
        println("Try word ${lastWord}")

        val input = readln()

        val newPredicates = input.mapIndexed { index, c ->
            when(c) {
                'g' -> PositionalPredicate(lastWord!!.toCharArray()[index], index)
                'y' -> AnyPositionPredicate(lastWord!!.toCharArray()[index])
                'b' -> NotInWordPredicate(lastWord!!.toCharArray()[index])
                else -> throw IllegalStateException("Illegal")
            }
        }

        solver.addPredicates(newPredicates)
    }
}