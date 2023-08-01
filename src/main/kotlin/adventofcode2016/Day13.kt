package adventofcode2016

import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

fun main() {
    Day13(test=false).showResult()
}

class Day13(test: Boolean) : PuzzleSolverAbstract(test) {
    private val favoriteNumber = if (test) 10 else 1352

    override fun resultPartOne(): Any {
        return shortestPath(if (test) pos(7,4) else pos(31,39))
    }

    override fun resultPartTwo(): Any {
        return maxDistinctPoints()
    }

    private fun shortestPath(goal: Point): Int {
        val alreadySeen = mutableSetOf<Point>()
        val queue = ArrayDeque<Pair<Point, Int>>().apply { add(Pair(pos(1,1), 0)) }
        while (queue.isNotEmpty()) {
            val (currentPos, stepsDone) = queue.removeFirst()
            if (currentPos == goal)
                return stepsDone

            alreadySeen += currentPos
            queue.addAll( currentPos.neighbors().filter{it.isOpen() && it !in alreadySeen }.map{Pair(it, stepsDone+1) })
        }
        return -1
    }

    private fun maxDistinctPoints(maxSteps: Int = 50): Int {
        val alreadySeen = mutableSetOf<Point>()
        val queue = ArrayDeque<Pair<Point, Int>>().apply { add(Pair(pos(1,1), 0)) }
        while (queue.isNotEmpty()) {
            val (currentPos, stepsDone) = queue.removeFirst()
            if (stepsDone > maxSteps)
                continue

            alreadySeen += currentPos
            queue.addAll( currentPos.neighbors().filter{it.isOpen() && it !in alreadySeen }.map{Pair(it, stepsDone+1) })
        }
        return alreadySeen.size
    }

    private fun Point.isOpen() =
        with(this) {
            (x >=0 && y >=0) &&
            (x*x + 3*x + 2*x*y + y + y*y + favoriteNumber).toString(2).count{ch -> ch == '1'} % 2 == 0
        }
}


