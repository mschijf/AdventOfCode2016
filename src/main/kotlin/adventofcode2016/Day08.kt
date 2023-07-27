package adventofcode2016

import java.lang.Math.floorMod

fun main() {
    Day08(test=false).showResult()
}

class Day08(test: Boolean) : PuzzleSolverAbstract(test) {

    private val rowSize = if (test) 3 else 6
    private val colSize = if (test) 7 else 50
    private val grid = List(rowSize){ MutableList(colSize){'.'} }

    override fun resultPartOne(): Any {
        inputLines.forEach {action ->
            when {
                action.startsWith("rect") -> grid.rect(action)
                action.startsWith("rotate row") -> grid.rotateRow(action)
                action.startsWith("rotate col") -> grid.rotateColumn(action)
                else -> throw Exception("Unexpected")
            }
        }
        grid.print()
        println()

        return grid.sumOf{row -> row.count { c -> c == '#' }}
    }

    override fun resultPartTwo(): Any {
        return "see above ^^^^"
    }

    private fun List<MutableList<Char>>.rect(raw: String) {
        val cols = raw.substringAfter("rect ").substringBefore("x").trim().toInt()
        val rows = raw.substringAfter("x").trim().toInt()
        for (row in 0..< rows) {
            for (col in 0..< cols) {
                this[row][col] = '#'
            }
        }
    }

    private fun List<MutableList<Char>>.rotateColumn(raw: String) {
        val col = raw.substringAfter("rotate column x=").substringBefore(" by").trim().toInt()
        val by = raw.substringAfter("by ").trim().toInt()
        val org = this.indices.map{row -> this[row][col]}
        for (row in this.indices) {
            this[row][col] = org[floorMod(row-by, rowSize)]
        }
    }

    private fun List<MutableList<Char>>.rotateRow(raw: String) {
        val row = raw.substringAfter("rotate row y=").substringBefore(" by").trim().toInt()
        val by = raw.substringAfter("by ").trim().toInt()
        val org = this[row].toList()
        for (col in this[row].indices) {
            this[row][col] = org[floorMod(col-by, colSize)]
        }
    }


    private fun List<List<Char>>.print() {
        this.indices.forEach { row ->
            this[row].indices.forEach { col ->
                print(this[row][col].toString().repeat(2))
            }
            println()
        }
    }

}




