package adventofcode2016

import tool.coordinate.twodimensional.Direction
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import tool.mylambdas.toMD5Hexadecimal
import kotlin.math.max

fun main() {
    Day17(test=false).showResult()
}

class Day17(test: Boolean) : PuzzleSolverAbstract(test) {

    private val passCode = if (test) "ulqzkmiv" else "udskfozm"
    private val endPos = pos(3,3)
    private val startPos = pos(3,3)

    override fun resultPartOne(): Any {
        return solve()
    }

    override fun resultPartTwo(): Any {
        return solvePart2()
    }

    private fun solve(): String {
        val queue = ArrayDeque<Pair<Point, String>>().apply { this.addLast(Pair(startPos, "")) }
        while (queue.isNotEmpty()) {
            val (currentPos, pathWalked) = queue.removeFirst()
            if (currentPos == endPos)
                return pathWalked

            queue.addAll (
                Direction.entries
                    .map{dir -> Pair(dir, currentPos.moveOneStep(dir))}
                    .filter { (dir, pos) -> pos.inGrid() && dir.isReachable(pathWalked) }
                    .map{(dir, pos) -> Pair(pos, pathWalked+dir.directionLetter)}
            )
        }
        return "no path found"
    }

    private fun solvePart2(): Int {
        var maxPath = -1
        val queue = ArrayDeque<Pair<Point, String>>().apply { this.addLast(Pair(pos(0,0), "")) }
        while (queue.isNotEmpty()) {
            val (currentPos, pathWalked) = queue.removeFirst()
            if (currentPos == endPos) {
                maxPath = max(maxPath, pathWalked.length)
            } else {
                queue.addAll (
                    Direction.entries
                        .map{dir -> Pair(dir, currentPos.moveOneStep(dir))}
                        .filter { (dir, pos) -> pos.inGrid() && dir.isReachable(pathWalked) }
                        .map{(dir, pos) -> Pair(pos, pathWalked+dir.directionLetter)}
                )
            }
        }
        return maxPath
    }


    private fun Point.inGrid() =
        this.x in (0..3) && this.y in (0..3)

    //
    //Only the first four characters of the hash are used;
    // they represent, respectively, the doors up, down, left, and right from your current position.
    // Any b, c, d, e, or f means that the corresponding door is open;
    // any other character (any number or a) means that the corresponding door is closed and locked.
    //
    private fun Direction.isReachable(pathToDoor: String) : Boolean {
        val key = (passCode + pathToDoor).toMD5Hexadecimal().take(4)
        return when (this) {
            Direction.UP -> key[0] in "bcdef"
            Direction.DOWN -> key[1] in "bcdef"
            Direction.LEFT -> key[2] in "bcdef"
            Direction.RIGHT -> key[3] in "bcdef"
        }
    }
}


