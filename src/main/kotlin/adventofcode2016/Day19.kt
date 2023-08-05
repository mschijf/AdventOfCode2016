package adventofcode2016

import tool.collectionspecials.MutableCircularLinkedList

fun main() {
    Day19(test=false).showResult()
}

class Day19(test: Boolean) : PuzzleSolverAbstract(test) {

    private val numberOfElves = if (test) 5 else 3_005_290

    override fun resultPartOne(): Any {

        val elfList = initElfList()

        var elfPos = elfList.firstNodeOrNull()!!
        while (elfList.size != 1) {
            val nextPos = elfPos.plus(1)
            elfPos.data = Pair(elfPos.data.first, elfPos.data.second + nextPos.data.second)
            elfList.removeAt(nextPos)
            elfPos = elfPos.plus(1)
        }
        return elfList.first().first
    }

    override fun resultPartTwo(): Any {
        val elfList = initElfList()

        var elfPos = elfList.firstNodeOrNull()!!
        var opposite = elfPos.plus(elfList.size/2)

        while (elfList.size != 1) {
            val stealFromElf = opposite
            opposite = opposite.plus(if (elfList.size % 2 == 1) 1 else -1)
            elfList.removeAt(stealFromElf)

            val presentStolen = stealFromElf.data.second
            elfPos.data = Pair(elfPos.data.first, elfPos.data.second + presentStolen)
            //elfList[elfPos] = Pair(elfPos.data.first, elfPos.data.second + presentStolen)

            elfPos = elfPos.plus(1)
            opposite = opposite.plus( 1 )
        }
        return elfList.first().first
    }

    private fun initElfList(): MutableCircularLinkedList<Pair<Int, Int>> {
        val cll = MutableCircularLinkedList<Pair<Int, Int>>()
        (1..numberOfElves).forEach { elfPos ->
            cll.add(Pair(elfPos, 1))
        }
        return cll
    }
}


