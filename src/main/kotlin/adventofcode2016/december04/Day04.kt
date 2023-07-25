package adventofcode2016.december04

import adventofcode2016.PuzzleSolverAbstract

fun main() {
    Day04(test=false).showResult()
}

class Day04(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        return inputLines
            .map {RoomInfo.of(it)}
            .filter {room -> room.letters.toCheckSum() == room.checkSum}
            .sumOf { it.sectorID }
    }

    override fun resultPartTwo(): Any {
        return inputLines
            .map {RoomInfo.of(it)}
            .map {Pair(it.decrypt(), it.sectorID) }
            .filter{it.first.contains("north")}
    }

    private fun RoomInfo.decrypt() =
        this.letters.map{if (it == '-') ' ' else it.decrypt(this.sectorID)}.joinToString("")

    private fun Char.decrypt(rotate: Int) =
        'a' + (((this - 'a') + rotate) % 26)

    private fun String.toCheckSum() =
        this.filterNot{it == '-'}
            .groupingBy { it }.eachCount()
            .toList()
            .sortedByDescending { (letter, count) -> 100*count + (26 - (letter - 'a')) }
            .take(5)
            .map{(letter, count) -> letter}
            .joinToString("")

}

data class RoomInfo(val letters: String, val sectorID: Int, val checkSum:String) {
    companion object {
        fun of(input: String) =
            RoomInfo(
                letters = input.substringBeforeLast("-"),
                sectorID = input.substringAfterLast("-").substringBefore("[").toInt(),
                checkSum = input.substringAfter("[").substringBefore("]"),
            )
    }
}

