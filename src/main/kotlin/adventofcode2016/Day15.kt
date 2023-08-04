package adventofcode2016

import tool.mylambdas.substringBetween

fun main() {
    Day15(test=false).showResult()
}

class Day15(test: Boolean) : PuzzleSolverAbstract(test) {

    private val discList = inputLines.map{Disc.of(it)}

    override fun resultPartOne(): Any {
        return generateSequence (0) { it + 1 }
            .first{
                startTime -> discList.withIndex().all{(listPos, disc) -> disc.isOpenAfter(startTime+listPos+1)}
            }
    }

    override fun resultPartTwo(): Any {
        val newDiscList = (discList + Disc(size=11, currentPos = 0))
        return generateSequence (0) { it + 1 }
            .first{
                startTime -> newDiscList.withIndex().all{(listPos, disc) -> disc.isOpenAfter(startTime+listPos+1)}
            }
    }
}

data class Disc(val size: Int, val currentPos: Int) {
    fun isOpenAfter(time: Int) =
        (currentPos+time) % size == 0

    companion object {
        fun of (rawInput: String) =
            Disc(
                size = rawInput.substringBetween("has ", " positions").toInt(),
                currentPos = rawInput.substringBetween("position ", ".").toInt()
            )
    }

}


