package adventofcode2016.december06

import adventofcode2016.PuzzleSolverAbstract

fun main() {
    Day06(test=false).showResult()
}

class Day06(test: Boolean) : PuzzleSolverAbstract(test) {

    private val letterColumns = (0..< inputLines.first().length).map{ column-> inputLines.map{it[column]}}

    override fun resultPartOne(): Any {
        return letterColumns
            .map{column -> column.groupingBy { it }.eachCount().maxBy { it.value }.key}
            .joinToString("")
    }

    override fun resultPartTwo(): Any {
        return letterColumns
            .map{column -> column.groupingBy { it }.eachCount().minBy { it.value }.key}
            .joinToString("")
    }
}


