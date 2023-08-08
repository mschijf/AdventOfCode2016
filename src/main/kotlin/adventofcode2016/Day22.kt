package adventofcode2016

import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import tool.coordinate.twodimensional.printAsGrid
import tool.mylambdas.collectioncombination.asCombinedItemsSequence
import tool.mylambdas.substringBetween
import kotlin.math.round

fun main() {
    Day22(test=false).showResult()
}

// /dev/grid/node-x0-y0     88T   66T    22T   75%

class Day22(test: Boolean) : PuzzleSolverAbstract(test) {

    private val fileList = inputLines.drop(2).map { Directory.of(it) }

    override fun resultPartOne(): Any {
        return fileList.asCombinedItemsSequence()
            .filter{(dir1, dir2) -> dir1.fitsInOther(dir2) || dir2.fitsInOther(dir1) }
            .count()
    }


    /**
     * Uitgezocht welke punten alles kunnen hebben elk welke te groot zijn
     * uitgezocht waar het 0 punt is
     * dit uitgeprint in een grid om er eens naar te kijken
     * gezien dat je het nu punt naar (maxX-1,0) kan brengen
     * en vaan daar stapje voor stapje dichter naar het beginpunt kan komen
     *
     * van start-0-punt naar (maxX-1,0) = 80 stappen
     * en dan kost het 5 stappen om van ".G-" naar "G-." te komen en een stap om van ".-G" naar ".G-" te komen
     * en dus (maxX-1)*5 + 1
     *
     * 261 is ok:  80 +   (maxX-1) * 5 + 1
     *
     * De situatie waarin we meer dan één nul hebben niet onderzocht en ook niet als er blokkades zouden zijn op de 0 rij
     *
     */
    override fun resultPartTwo(): Any {
//        val grid = fileList.associate { node -> node.point to Pair(node.used, fileList.filter{ it != node}.all{ node.used <= it.total}) }
//        grid.printAsGrid { (used, fitting) -> if (used == 0) "0" else if (fitting) "." else "#" }
        val grid = fileList.filter { node -> fileList.filter{ it != node}.all{ node.used <= it.total} }.map{it.point}.toSet()
        val zero = fileList.first {it.used == 0}

        val maxX = grid.filter{it.y==0}.maxOf { it.x }
        return (maxX-1)*5 + 1 + grid.shortestPath(zero.point, pos(maxX-1, 0))
    }

    private fun Directory.fitsInOther(other: Directory) =
        this.used != 0 && this.used <= other.available()

    private fun Set<Point>.shortestPath(from: Point, to: Point): Int {
        val alreadySeen = mutableSetOf<Point>()
        val queue = ArrayDeque<Pair<Point, Int>>().apply { this.add(Pair(from,0)) }
        while (queue.isNotEmpty()) {
            val (currentPos, stepsDone) = queue.removeFirst()

            if (currentPos == to)
                return stepsDone

            currentPos
                .neighbors()
                .filter { nb -> nb in this@shortestPath }
                .filter { nb -> nb !in alreadySeen}
                .forEach { nb ->
                    alreadySeen += nb
                    queue.addLast(Pair(nb, stepsDone + 1))
                }
        }
        return -1
    }
}

data class Directory(val point: Point, val total: Int, val used: Int) {
    fun available() =
        total - used

    companion object {
        fun of(rawInput: String): Directory {
            val fields = rawInput.split("\\s".toRegex()).filter { it.isNotEmpty() }
            return Directory(
                point = pos( fields[0].substringBetween("-x","-").toInt(), fields[0].substringAfter("y").toInt()),
                total = fields[1].dropLast(1).toInt(),
                used = fields[2].dropLast(1).toInt()
            )
        }
    }
}


