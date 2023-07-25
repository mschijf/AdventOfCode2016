package adventofcode2016.december04

import adventofcode2016.PuzzleSolverAbstract

fun main() {
    Day04(test=false).showResult()
}

class Day04(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        val x = inputLines
            .map {it.splitRoomInfo()}
            .filter {room -> room.letters.toCheckSum() == room.checkSum}
        return x.sumOf { it.sectorID }
    }

    override fun resultPartTwo(): Any {
        val x = inputLines
            .map {it.splitRoomInfo()}
            .map {Pair(it.decrypt(), it.sectorID) }
            .filter{it.first.contains("north")}
        return x
    }

    private fun RoomInfo.decrypt(): String {
        return this.letters.map{if (it == '-') ' ' else it.decrypt(this.sectorID)}.joinToString("")
    }

    private fun Char.decrypt(rotate: Int): Char {
        return 'a' + (((this - 'a') + rotate) % 26)
    }

    private fun String.toCheckSum() =
        this.filterNot{it == '-'}
            .groupingBy { it }.eachCount()
            .toList()
            .sortedByDescending { (letter, count) -> 100*count + (26 - (letter - 'a')) }
            .take(5)
            .map{it.second}
            .joinToString("")

    private fun String.splitRoomInfo(): RoomInfo {
        return RoomInfo(
            letters = this.substringBeforeLast("-"),
            sectorID = this.substringAfterLast("-").substringBefore("[").toInt(),
            checkSum = this.substringAfter("[").substringBefore("]"),
            )
    }
}

data class RoomInfo(val letters: String, val sectorID: Int, val checkSum:String)

