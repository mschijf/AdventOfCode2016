package adventofcode2016.december03

import adventofcode2016.PuzzleSolverAbstract

fun main() {
    Day03(test=false).showResult()
}

class Day03(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        return inputLines
            .map { line -> line
                .trim()
                .split("\\s+".toRegex())
                .map{ it.toInt() }
                .sorted()
            }
            .count { it[0] + it[1] > it[2] }
    }

    override fun resultPartTwo(): Any {
        val input = inputLines
            .map { line -> line.trim().split("\\s+".toRegex()).map { it.toInt() } }

        return with(input) {
            (0..input.first().size).map { i -> map { it[i] } } //transposing over 3 columns -> 3 lists
                .flatten()//one big list
                .chunked(3)  //list of lists of 3 elements
                .map { it.sorted() } // sort the small lists
                .count { it[0] + it[1] > it[2] } //check for triangle
        }
    }
}


