package adventofcode2016

fun main() {
    Day18(test=false).showResult()
}

class Day18(test: Boolean) : PuzzleSolverAbstract(test) {

    private val firstRow = inputLines.first().map{it}

    override fun resultPartOne(): Any {
        return countSafeTilesAfter(if (test) 10 else 40)
    }

    override fun resultPartTwo(): Any {
        return countSafeTilesAfter(400_000)
    }

    private fun countSafeTilesAfter(rowsToBeExamined: Int): Int {
        var currentRow = firstRow
        var safeTiles = firstRow.safeTileCount()
        repeat(rowsToBeExamined-1) {
            currentRow = currentRow.nextRow()
            safeTiles += currentRow.safeTileCount()
        }
        return safeTiles
    }

    private fun List<Char>.safeTileCount() =
        this.count { ch -> ch == '.' }

    private fun List<Char>.nextRow() =
        List(this.size) { index ->  if (index.isTrap(this)) '^' else '.'}

    /**
     * Reading it well gives us that a tile is a trap if in previous row, the following is true:
     *        (left && center && !right) ||
     *        (!left && center && right) ||
     *        (left && !center && !right) ||
     *        (!left && !center && right)
     * So, it doesn't matter what the vallue of center is, and these rules can be reduced to
     *        (left && center && !right) ||
     *        (!left && center && right)
     * Which can be reduced to
     *        (left != right)
     */
    private fun Int.isTrap(previousRow: List<Char>): Boolean {
        val left = if (this == 0) '.' else previousRow[this-1]
        val right = if (this == previousRow.size-1) '.' else previousRow[this+1]

        return (left != right)
    }
}


