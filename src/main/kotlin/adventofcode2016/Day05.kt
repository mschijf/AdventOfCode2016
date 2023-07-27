package adventofcode2016

import java.math.BigInteger
import java.security.MessageDigest

fun main() {
    Day05(test=false).showResult()
}

class Day05(test: Boolean) : PuzzleSolverAbstract(test) {
    private val doorID = if (test) "abc" else "ffykfhsq"
    private val crypt = MessageDigest.getInstance("MD5")

    override fun resultPartOne(): Any {

        return generateSequence (0){ it+1 }
            .map {(doorID + it).toMD5Hexadecimal()}
            .filter { it.startsWith("00000") }
            .take(8)
            .map{it[5]}
            .joinToString("")

//        var result = ""
//        repeat(10_000_000) {index ->
//            val v = (doorID + index.toString()).toMD5Hexadecimal()
//            if (v.startsWith("00000")) {
//                result += v[5]
//                if (result.length == 8)
//                    return result
//            }
//        }
//        return -1
    }

    override fun resultPartTwo(): Any {
        val set = mutableSetOf<Int>()
        return generateSequence (0){ it+1 }
            .map {(doorID + it).toMD5Hexadecimal()}
            .filter { it.startsWith("00000") }
            .filter { val ch = it[5] - '0'; ch in 0 .. 7 && ch !in set }
            .map { Pair(it[5] - '0', it[6]).also { p-> set+=p.first }  }
            .take(8)
            .toList()
            .sortedBy { it.first }
            .joinToString(""){it.second.toString()}
//
//
//        val result = charArrayOf('-', '-', '-', '-', '-', '-', '-', '-')
//        var count = 0
//        var index=0
//        while (true) {
//            val v = (doorID + index.toString()).toMD5Hexadecimal()
//            if (v.startsWith("00000")) {
//                val digit = v[5] - '0'
//                if (v[5].isDigit() && (digit < 8) && (result[digit] == '-') ) {
//                    result[v[5] - '0'] = v[6]
//                    count ++
//                    if (count >= 8)
//                        return "$index --> ${result.joinToString("")}"
//                }
//            }
//            index++
//        }
//        return -1
    }



    private fun String.toMD5Hexadecimal(): String {
        crypt.update(this.toByteArray())
        return crypt.digest().toHex()
    }

    private fun ByteArray.toHex(): String {
        val tmp = BigInteger(1, this).toString(16)
        return "0".repeat(32-tmp.length) + tmp
    }
}


