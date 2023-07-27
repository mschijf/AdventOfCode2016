package adventofcode2016.december07

import adventofcode2016.PuzzleSolverAbstract

fun main() {
    Day07(test=false).showResult()
}

class Day07(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        return inputLines
            .map{it.breakInParts()}
            .filter{ it.none{ (letters, hypermode) -> hypermode && letters.hasAbba()} }
            .filter{ it.any{ (letters, hypermode) -> !hypermode && letters.hasAbba()} }
            .count()
    }

    override fun resultPartTwo(): Any {
        val allStrings = inputLines.map{it.breakInParts()}

        //make per line a list of aba occurences appearing in hyperMode blocks -> resulting in list of list<aba-strings>
        val hyperList = allStrings
            .map{it.filter{(_, hypermode) -> hypermode}.map{(letters, _) -> letters}}
            .map{it.flatMap{letters -> letters.listOfABA()}}

        //make per line a list of aba occurences appearing in non-hyperMode blocks -> resulting in list of list<aba-strings>
        val nonHyperList = allStrings
            .map{it.filter{(_, hypermode) -> !hypermode}.map{(letters, _) -> letters}}
            .map{it.flatMap{letters -> letters.listOfABA()}}

        //combine both lists and check
        //   if there is at least one aba occurence in hyper that is existing as bab occurence in non-hyper
        return hyperList.zip(nonHyperList)
            .count { (hyperABAList, nonHyperABAList) -> hyperABAList.hasCorrespondingBAB(nonHyperABAList) }
    }

    private fun String.hasAbba() =
        this.windowed(4).any { it[0] == it[3] && it[1] == it[2] && it[0] != it[1]}

    private fun String.listOfABA() =
        this.windowed(3).filter { it[0] == it[2] && it[1] != it[2]}

    private fun List<String>.hasCorrespondingBAB(list: List<String>) =
        this.map{s -> s.abaToBab()}.any { bab -> list.any {aba -> aba == bab} }

    private fun String.abaToBab() =
        "${this[1]}${this[0]}${this[1]}"

    private fun String.breakInParts(): List<Pair<String, Boolean>> {
        val result = mutableListOf<Pair<String, Boolean>>()
        var current = ""
        var i = 0
        while (i < this.length) {
            if (this[i] in "[]") {
                result += Pair(current, this[i] == ']')
                current = ""
            } else {
                current += this[i]
            }
            i++
        }
        if (current.isNotEmpty())
            result += Pair(current,false)
        return result
    }
}


