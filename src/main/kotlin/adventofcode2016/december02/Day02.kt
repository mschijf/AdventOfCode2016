package adventofcode2016.december02

import adventofcode2016.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Direction
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

fun main() {
    Day02(test=false).showResult()
}

class Day02(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        val legalPos = (1..9).map{pos((it-1) % 3, (it-1) / 3 ) }.toSet()
        var result = ""
        var current = pos(1,1)
        inputLines.forEach { line ->
            current = current.followSequence(line, legalPos)
            result += current.toNumber()
        }
        return result
    }

    override fun resultPartTwo(): Any {
        val legalPos = setOf(
            pos(2,0),
            pos(1,1),pos(2,1),pos(3,1),
            pos (0,2), pos(1,2),pos(2,2),pos(3,2),pos(4,2),
            pos(1,3),pos(2,3),pos(3,3),
            pos(2,4),
            )
        var result = ""
        var current = pos(0,2)
        inputLines.forEach { line ->
            current = current.followSequence(line, legalPos)
            result += current.toNumberComplex(legalPos)
        }
        return result
    }

    private fun Point.followSequence(sequence:String, legalPos: Set<Point>): Point {
        var pos = this
        sequence.forEach { ch ->
            val dir = Direction.ofLetter(ch.toString())
            val nextPos = pos.moveOneStep(dir)
            if (nextPos in legalPos)
                pos = nextPos
        }
        return pos
    }

    private fun Point.toNumber() = this.y*3 + this.x + 1

    private fun Point.toNumberComplex(points: Set<Point>): Char {
        val i = points.indexOf(this)
        return if (i < 9) '1' + i else 'A' + (i-9)
    }

}


