package adventofcode2016

import java.lang.StringBuilder

fun main() {
    Day21(test=false).showResult()
}

class Day21(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        val operationList = inputLines.map{it.toOperation()}
        val input = if (test) "abcde" else "abcdefgh"
        return operationList.fold(input){ acc, function -> function(acc) }
    }

    override fun resultPartTwo(): Any {
        val operationList = inputLines.reversed().map{it.toReverseOperation()}
        val input = if (test) "decab" else "fbgdceah"
        return operationList.fold(input){ acc, function -> function(acc) }
    }

}

fun String.toOperation(): (String) -> String {
    val words = this.split(" ")
    val operator = words[0] + " " + words[1]
    return when (operator) {
        "swap position" -> { input -> input.swapByPos(words[2].toInt(), words[5].toInt()) }
        "swap letter" -> { input -> input.swapByLetterPos(words[2].first(), words[5].first()) }
        "reverse positions" -> { input -> input.reverse(words[2].toInt(), words[4].toInt()) }
        "rotate left" -> { input -> input.rotateLeft(words[2].toInt()) }
        "rotate right" -> { input -> input.rotateRight(words[2].toInt()) }
        "rotate based" -> { input -> input.rotateRightByLetterPos(words[6].first()) }
        "move position" -> { input -> input.movePosition(words[2].toInt(), words[5].toInt()) }
        else -> throw Exception("Unexpected operator")
    }
}

fun String.toReverseOperation(): (String) -> String {
    val words = this.split(" ")
    val operator = words[0] + " " + words[1]
    return when (operator) {
        "swap position" -> { input -> input.swapByPos(words[2].toInt(), words[5].toInt()) }
        "swap letter" -> { input -> input.swapByLetterPos(words[2].first(), words[5].first()) }
        "reverse positions" -> { input -> input.reverse(words[2].toInt(), words[4].toInt()) }
        "rotate left" -> { input -> input.rotateRight(words[2].toInt()) }
        "rotate right" -> { input -> input.rotateLeft(words[2].toInt()) }
        "rotate based" -> { input -> input.rotateLeftByLetterPos(words[6].first()) }
        "move position" -> { input -> input.movePosition(words[5].toInt(), words[2].toInt()) }
        else -> throw Exception("Unexpected operator")
    }
}



fun String.swapByPos(par1: Int, par2: Int): String {
    val input = this.toCharArray()
    val tmp = input[par1]
    input[par1] = input[par2]
    input[par2] = tmp
    return input.concatToString()
}

fun String.swapByLetterPos(par1: Char, par2: Char): String {
    return this.swapByPos(this.indexOf(par1), this.indexOf(par2))
}

fun String.reverse(par1: Int, par2: Int): String {
    return this.substring(0, par1) + this.substring(par1, par2+1).reversed() + this.substring(par2+1)
}

fun String.rotateLeft(by: Int): String {
    val new = StringBuilder()
    for (i in this.indices) {
        new.append( this[(i+by) % this.length] )
    }
    return new.toString()
}

fun String.rotateRight(by: Int): String {
    val new = StringBuilder()
    for (i in this.indices) {
        new.append( this[(2*this.length + i - by) % this.length] )
    }
    return new.toString()
}

fun String.movePosition(pos1: Int, pos2: Int): String {
    val letter = this[pos1]
    val tmp = this.substring(0, pos1) + this.substring(pos1+1)
    return tmp.substring(0,pos2) + letter + tmp.substring(pos2)
}

fun String.rotateRightByLetterPos(byLetter: Char): String {
    val n = this.indexOf(byLetter)
    return rotateRight(n + 1 + (if (n>=4) 1 else 0))
}

/**
 * Het is niet eenduidig om de reverse functie van 'rotateRightByLetterPos' te bepalen
 *
 * Daarom maar bruteforce: probeer een rotateleft, voer op dat resultaat de rotateRightByLetterPos uit en kijk of we
 * op het oorspronkelijke uitkomen. Zo ja, dan is dat de reverse.
 *
 */
fun String.rotateLeftByLetterPos(byLetter: Char): String {
    for (i in this.indices) {
        val result = this.rotateLeft(i)
        if (result.rotateRightByLetterPos(byLetter) == this)
            return result
    }
    throw Exception("Zou niet mogelijk moeten zijn")
}

