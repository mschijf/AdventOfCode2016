package adventofcode2016

import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import tool.mylambdas.collectioncombination.mapCombinedItems

fun main() {
    Day24(test=false).showResult()
}

class Day24(test: Boolean) : PuzzleSolverAbstract(test) {

    private val gridFields = inputLines
        .flatMapIndexed { y: Int, row: String ->
            row.mapIndexed {x, ch -> if (ch != '#') pos(x,y) else null }
        }.filterNotNull().toSet()

    private val numbers = inputLines
        .flatMapIndexed { y: Int, row: String ->
            row.mapIndexed {x, ch -> if (ch != '#' && ch != '.') Pair(ch, pos(x,y)) else null }
        }.filterNotNull().toMap()

    private val distanceMap = numbers.keys
        .toList()
        .sorted()
        .mapCombinedItems { from, to -> Pair(from, to) to shortestPath(numbers[from]!!, numbers[to]!!)}
        .toMap()

    override fun resultPartOne(): Any {
        return shortestPathToAll()
    }

    override fun resultPartTwo(): Any {
        return shortestPathToAll(backToStart = true)
    }

    private fun shortestPath(from: Point, to: Point) : Int {
        val alreadySeen = mutableSetOf(from)
        val queue = ArrayDeque<Pair<Point, Int>>().apply { this.add( Pair(from,0) ) }
        while (queue.isNotEmpty()) {
            val (currentPos, stepsDone) = queue.removeFirst()
            if (currentPos == to) {
                return stepsDone
            }

            currentPos.neighbors().filter { nb -> nb in gridFields }.filter{nb -> nb !in alreadySeen}.forEach {newPos ->
                alreadySeen += newPos
                queue.add(Pair(newPos, stepsDone+1))
            }
        }
        return -1
    }

    private fun shortestPathToAll(from: Char = '0', backToStart: Boolean = false, pathDone: Set<Char> = setOf(from)) : Int {
        return if (pathDone.size == numbers.keys.size) {

            if (backToStart) from.distanceTo('0') else 0

        } else {

            numbers.keys.filter { number -> number !in pathDone }.minOf { newNumber ->
                shortestPathToAll(newNumber, backToStart, pathDone+newNumber) + from.distanceTo(newNumber)
            }

        }
    }

    private fun Char.distanceTo(other:Char) =
        distanceMap.getOrDefault( if (this < other) Pair(this, other) else Pair(other, this), -999_999 )
}


