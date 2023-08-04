package adventofcode2016

import tool.mylambdas.toMD5Hexadecimal
import java.security.MessageDigest

fun main() {
    Day14(test=false).showResult()
}

class Day14(test: Boolean) : PuzzleSolverAbstract(test) {
    private val salt = if (test) "abc" else "qzyelonm"
    private val crypt = MessageDigest.getInstance("MD5")

    override fun resultPartOne(): Any {
        val md5List = (0..100_000)
            .map {Pair((salt + it).toMD5Hexadecimal(), it)}
            .map { Triple(it.first, it.second, it.first.firstTripletOrNull()?:'-') }
            .filter { it.third != '-' }

        return md5List.filterIndexed { index, _ -> md5List.checkNext1000(index) }.take(64).last()
    }

    /**
     * de 22_000 is zo gezet, nadat het antword bekend was. Eerder met 100_000 gewerkt en toen
     * kostte het zo'n 83 seconden.
     */
    override fun resultPartTwo(): Any {
        val md5List = (0..22_000)
            .map {Pair((salt + it).toStretchedMD5Hash(), it)}
            .map { Triple(it.first, it.second, it.first.firstTripletOrNull()?:'-') }
            .filter { it.third != '-' }

        return md5List.filterIndexed { index, _ -> md5List.checkNext1000(index) }.take(64).last()
    }

    private fun String.toStretchedMD5Hash(): String {
        var x = this
        repeat(2017) {
            x = x.toMD5Hexadecimal()
        }
        return x
    }

    private fun List<Triple<String, Int, Char>>.checkNext1000(from:Int): Boolean {
        var i = from+1
        while (i < this.size && this[i].second < this[from].second+1000) {
            if (this[i].first.hasQuintletWith(this[from].third)) {
                return true
            }
            i++
        }
        return false
    }

    private fun String.firstTripletOrNull() =
        this.withIndex().firstOrNull(){it.index < this.length-2 && it.value == this[it.index+1] && it.value == this[it.index+2]}?.value

    private fun String.hasQuintletWith(ch: Char) =
        this.contains("$ch$ch$ch$ch$ch")

}

