package adventofcode2016

import java.lang.StringBuilder

fun main() {
    Day16(test=false).showResult()
}

class Day16(test: Boolean) : PuzzleSolverAbstract(test) {

    val input = if (test) "10000" else "01110110101001000"

    override fun resultPartOne(): Any {
        val diskSize = if (test) 20 else 272
        return input.fillDisk(diskSize).take(diskSize).checkSum()
    }

    override fun resultPartTwo(): Any {
        val diskSize = if (test) 20 else 35_651_584
        return input.fillDisk(diskSize).take(diskSize).checkSum()
    }

    private fun String.fillDisk(maxSize: Int) : String {
        var start = this
        while (start.length < maxSize)
            start = start.expand()
        return start
    }

    private fun String.expand() : String {
        val a = this
        val b = a.reversed().swapBits()
        return "${a}0${b}"
    }

    private fun String.swapBits(): String {
        val b = StringBuilder()
        this.forEach { bit -> b.append(if (bit == '1') '0' else '1') }
        return b.toString()
    }

    private fun String.checkSum(): String {
        var checksum = this
        while (checksum.length % 2 == 0) {
            checksum = checksum.checkSumRound()
        }
        return checksum
    }

    private fun String.checkSumRound(): String {
        val b = StringBuilder()
        for (i in indices step 2) {
            b.append(if (this[i] == this[i+1]) "1" else "0")
        }
        return b.toString()
    }

//    private fun String.checkSum(): String {
//        var checksum = this
//        while (checksum.length % 2 == 0) {
//            checksum = checksum.chunked(2).joinToString("") { if (it[0] == it[1]) "1" else "0" }
//        }
//        return checksum
//    }

}


