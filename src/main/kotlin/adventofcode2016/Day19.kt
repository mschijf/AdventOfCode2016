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
            val nextPos = elfPos.next()
            elfList[elfPos] = Pair(elfList[elfPos].first, elfList[elfPos].second + elfList[nextPos].second)
            elfList.removeAt(nextPos)
            elfPos = elfPos.next()
        }
        return elfList[elfList.firstIndexOrNull()!!].first
    }

    override fun resultPartTwo(): Any {
        val elfList = initElfList()
        var elfPos = elfList.firstIndex()
        var oppositePos = elfPos.next(elfList.size/2)

        while (elfList.size != 1) {
            val stealPos = oppositePos
            oppositePos = oppositePos.next(if (elfList.size % 2 == 1) 1 else -1)
            val (_, presentsStolen) = elfList.removeAt(stealPos)

            elfList[elfPos] = Pair(elfList[elfPos].first, elfList[elfPos].second + presentsStolen)

            elfPos = elfPos.next()
            oppositePos = oppositePos.next()
        }
        return elfList[elfList.firstIndexOrNull()!!].first
    }

    private fun initElfList() =
        (1..numberOfElves).map { Pair(it, 1) }.toCircularLinkedList()

}


