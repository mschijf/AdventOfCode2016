package tool.coordinate.twodimensional

enum class Direction(val directionSymbol: String) {
    UP("^") {
        override fun rotateRight() = RIGHT
        override fun rotateLeft() = LEFT
    },
    DOWN("v") {
        override fun rotateRight() = LEFT
        override fun rotateLeft() = RIGHT
    },
    RIGHT(">") {
        override fun rotateRight() = DOWN
        override fun rotateLeft() = UP
    },
    LEFT("<") {
        override fun rotateRight() = UP
        override fun rotateLeft() = DOWN
    };

    abstract fun rotateRight(): Direction
    abstract fun rotateLeft(): Direction
    override fun toString() = directionSymbol.toString()
    fun opposite() = rotateLeft().rotateLeft()

    companion object {
        fun of(s: String): Direction =
            Direction
                .values()
                .firstOrNull() { it.directionSymbol == s.uppercase() }
                ?: throw Exception("$s is not a symbol in Direction")
    }

}
