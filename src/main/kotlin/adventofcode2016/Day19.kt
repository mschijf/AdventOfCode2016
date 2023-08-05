package adventofcode2016

import tool.collectionspecials.CircularLinkedList

fun main() {
    Day19(test=false).showResult()
}

class Day19(test: Boolean) : PuzzleSolverAbstract(test) {

    private val numberOfElves = if (test) 5 else 3_005_290

    override fun resultPartOne(): Any {
        val elfList = CircularLinkedList<Pair<Int, Int>>()
        (1..numberOfElves).forEach { elfPos ->
            elfList.add(Pair(elfPos, 1))
        }

        var elfPos = elfList.firstOrNull()!!
        while (true) {
            if (elfPos.data.second == numberOfElves)
                return elfPos.data.first
            val nextPos = elfPos.plus(1)
            val newPos = elfList.addBefore(elfPos, Pair(elfPos.data.first, elfPos.data.second + nextPos.data.second))
            elfList.removeAt(elfPos)
            elfList.removeAt(nextPos)
            elfPos = newPos.plus(1)
        }
        return -1
    }

    override fun resultPartTwo(): Any {
        val elfList = CircularLinkedList<Pair<Int, Int>>()
        (1..numberOfElves).forEach { elfPos ->
            elfList.add(Pair(elfPos, 1))
        }

        var elfPos = elfList.firstOrNull()!!
        var opposite = elfPos.plus(elfList.size/2)

        while (true) {
//            println(elfList.map{it.first}.joinToString())
//            println("    ELFPOS: ${elfPos.data.first}, OPPOSITE: ${opposite.data.first}")
            if (elfPos.data.second == numberOfElves)
                return elfPos.data.first

            val stealFromElf = opposite
            opposite = opposite.plus(if (elfList.size % 2 == 1) 1 else -1)
            elfList.removeAt(stealFromElf)

            val presentStolen = stealFromElf.data.second
            val newPos = elfList.addBefore(elfPos, Pair(elfPos.data.first, elfPos.data.second + presentStolen))
            elfList.removeAt(elfPos)
            elfPos = newPos

            elfPos = elfPos.plus(1)
            opposite = opposite.plus( 1 )
        }
        return -1
    }
}


