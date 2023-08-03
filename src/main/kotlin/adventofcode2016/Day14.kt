package adventofcode2016

import java.math.BigInteger
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
            .map { Triple(it.first, it.second, it.first.firstTriplet()?:'-') }
            .filter { it.third != '-' }

        return md5List.filterIndexed { index, _ -> md5List.checkNext1000(index) }.take(64).last()
    }

    /**
     * de 22_000 is zo gezet, nadat het antword bekend was. Eerder met 100_000 gewerkt en toen
     * kostte het zo'n 83 seconden.
     */
    override fun resultPartTwo(): Any {
//        val md5List = (0..22_000)
//            .map {Pair((salt + it).toStretchedMD5Hash(), it)}
//            .map { Triple(it.first, it.second, it.first.firstTriplet()?:'-') }
//            .filter { it.third != '-' }
//
//        return md5List.filterIndexed { index, _ -> md5List.checkNext1000(index) }.take(64).last()

        val input = salt

        val keyGenerator = KeyGenerator(salt = input)

        println("Part 1: ${keyGenerator.generateKeys(64, applyKeyStretching = false).last()}")
        println("Part 2: ${keyGenerator.generateKeys(64, applyKeyStretching =  true).last()}")
        return ""
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

    private fun String.firstTriplet() =
        this.withIndex().firstOrNull(){it.index < this.length-2 && it.value == this[it.index+1] && it.value == this[it.index+2]}?.value

    private fun String.hasQuintletWith(ch: Char) =
        this.contains("$ch$ch$ch$ch$ch")

}

private fun String.toMD5Hexadecimal(): String {
    return MessageDigest.getInstance("MD5").digest(toByteArray()).toHexString()

//    crypt.update(this.toByteArray())
//    return crypt.digest().toHex()
}

fun ByteArray.toHexString() : String {
    val builder = StringBuilder()

    for (b in this) {
        builder.append(b.toHexString())
    }

    return builder.toString()
}

private val HEX_CHARS = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

fun Byte.toHexString() : String {
    val thisAsInt = this.toInt()
    val resultChar2 = HEX_CHARS[thisAsInt and 0x0f]
    val resultChar1 = HEX_CHARS[thisAsInt shr 4 and 0x0f]
    return "$resultChar1$resultChar2"
}

private fun ByteArray.toHex(): String {
    val tmp = BigInteger(1, this).toString(16)
    return "0".repeat(32-tmp.length) + tmp
}

class KeyGenerator(private val salt: String) {

    companion object {
        private val NUMBER_OF_HASHES_TO_LOOK_AHEAD = 1000
    }

    fun generateKeys(numberOfKeys: Int, applyKeyStretching: Boolean) : List<Int> {
        val result = mutableSetOf<Int>()
        val keyCandidates = mutableMapOf<Int, Char>()

        var currentIndex = 0
        var largestIndexToCheck = Integer.MAX_VALUE

        while (currentIndex < largestIndexToCheck) {
            var currentHash = "$salt$currentIndex".toMD5Hexadecimal()

            if (applyKeyStretching) {
                for (i in 1..2016) {
                    currentHash = currentHash.toMD5Hexadecimal()
                }
            }

            val keyCandidateEntriesIterator = keyCandidates.iterator()

            while (keyCandidateEntriesIterator.hasNext()) {
                val keyCandidateEntry = keyCandidateEntriesIterator.next()

                if (keyCandidateEntry.key + NUMBER_OF_HASHES_TO_LOOK_AHEAD < currentIndex) {
                    keyCandidateEntriesIterator.remove()
                } else if (currentHash.containsRepeatedChar(keyCandidateEntry.value, minRepetitions = 5)) {
                    result.add(keyCandidateEntry.key)

                    /*
                     * Once we've found the requested number of keys, check NUMBER_OF_HASHES_TO_LOOK_AHEAD more candidates in case
                     * there are smaller keys that we have yet to discover.
                     */
                    if (result.size == numberOfKeys && largestIndexToCheck == Integer.MAX_VALUE) {
                        largestIndexToCheck = currentIndex + NUMBER_OF_HASHES_TO_LOOK_AHEAD
                    }

                    keyCandidateEntriesIterator.remove()
                }
            }

            val firstCharRepeatedThreeTimes = currentHash.firstRepeatedChar(minRepetitions = 3)

            firstCharRepeatedThreeTimes?.let {
                keyCandidates.put(currentIndex, it)
            }

            currentIndex += 1
        }

        return result.take(numberOfKeys).sorted()
    }

}


fun String.allCharactersMatch(): Boolean {
    if (isEmpty()) {
        throw IllegalArgumentException("This method can only be called on non-empty Strings.")
    }

    return all { it == first() }
}

fun String.allCharactersMatch(char: Char): Boolean {
    if (isEmpty()) {
        throw IllegalArgumentException("This method can only be called on non-empty Strings.")
    }

    return all { it == char }
}

fun String.firstRepeatedChar(minRepetitions: Int): Char? {
    if (minRepetitions > length) {
        return null
    }

    (0..length - minRepetitions).forEach { startIndex ->
        if (substring(startIndex until startIndex + minRepetitions).allCharactersMatch()) {
            return get(startIndex)
        }
    }

    return null
}

fun String.containsRepeatedChar(char: Char, minRepetitions: Int): Boolean {
    if (minRepetitions > length) {
        return false
    }

    (0..length - minRepetitions).forEach { startIndex ->
        if (substring(startIndex until startIndex + minRepetitions).allCharactersMatch(char)) {
            return true
        }
    }

    return false
}