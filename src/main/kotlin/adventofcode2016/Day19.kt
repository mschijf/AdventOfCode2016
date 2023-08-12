package adventofcode2016

import tool.collectionspecials.toCircularLinkedList

fun main() {
    Day19(test=false).showResult()
}

class Day19(test: Boolean) : PuzzleSolverAbstract(test) {

    private val numberOfElves = if (test) 5 else 3_005_290

    override fun resultPartOne(): Any {
        val elfList = initElfList()

        var elfPos = elfList.firstIndex()
        while (elfList.size != 1) {
            val nextPos = elfPos + 1
            elfList[elfPos] = Pair(elfList[elfPos].first, elfList[elfPos].second + elfList[nextPos].second)
            elfList.removeAt(nextPos)
            elfPos++
        }
        return elfList[elfList.firstIndexOrNull()!!].first
    }

    override fun resultPartTwo(): Any {
        val elfList = initElfList()
        var elfPos = elfList.firstIndex()
        var oppositePos = elfPos + elfList.size/2

        while (elfList.size != 1) {
            val stealPos = oppositePos
            oppositePos += if (elfList.size % 2 == 1) 1 else -1
            val (_, presentsStolen) = elfList.removeAt(stealPos)

            elfList[elfPos] = Pair(elfList[elfPos].first, elfList[elfPos].second + presentsStolen)

            elfPos++
            oppositePos++
        }
        return elfList[elfList.firstIndexOrNull()!!].first
    }

    private fun initElfList() =
        (1..numberOfElves).map { Pair(it, 1) }.toCircularLinkedList()

}


