package adventofcode2016

import kotlin.math.max
import kotlin.math.min

fun main() {
    Day20(test=false).showResult()
}

class Day20(test: Boolean) : PuzzleSolverAbstract(test) {

    private val rangeList = inputLines.map{ rangeLine -> rangeLine.split("-").run { this[0].toLong() .. this[1].toLong() } }
    private val maxValue: Long = if (test) 9 else UInt.MAX_VALUE.toLong()

    override fun resultPartOne(): Any {
        val result = rangeList.fold(emptySet<LongRange>()){ acc, range -> acc.combine(range) }
        return result.minBy { it.first }.last+1
    }

    override fun resultPartTwo(): Any {
        val sortedResult = rangeList
            .fold(emptySet<LongRange>()){ acc, range -> acc.combine(range) }
            .sortedBy { it.first }
        val total = sortedResult
            .windowed(2)
            .sumOf { (range1, range2) -> (range2.first - (range1.last+1)) }
        return total + (maxValue - (sortedResult.last().last))
    }

    private fun Set<LongRange>.combine(otherRange: LongRange): Set<LongRange> {
        val new = mutableSetOf<LongRange>()
        var worker = otherRange
        this.forEach { aRange ->
            if (aRange.hasOverlap(otherRange)) {
                worker = combine(worker, aRange)
            } else {
                new.add(aRange)
            }
        }
        new.add(worker)
        return new
    }

    private fun LongRange.hasOverlap(otherRange: LongRange) =
        (this.last + 1) >= otherRange.first && this.first <= (otherRange.last + 1)

    private fun combine(range1: LongRange, range2: LongRange) =
        min(range1.first(), range2.first) ..max(range1.last, range2.last)

}


