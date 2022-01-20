sealed interface WordlePredicate {
    fun applyPredicate(s: String): Boolean

    val c: Char
}

data class AnyPositionPredicate(override val c: Char): WordlePredicate {
    override fun applyPredicate(s: String): Boolean {
        return s.contains(c)
    }
}

data class NotInWordPredicate(override val c: Char): WordlePredicate {
    override fun applyPredicate(s: String): Boolean {
        return !s.contains(c)
    }
}

data class PositionalPredicate(override val c: Char, val position: Int): WordlePredicate {
    override fun applyPredicate(s: String): Boolean {
        return s.toCharArray()[position] == c
    }
}
