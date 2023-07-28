package adventofcode2016

import tool.mylambdas.hasOnlyDigits

fun main() {
    Day09(test=false).showResult()
}

class Day09(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        inputLines.forEach{println(it.decompress().length)}
        return ""
    }

    override fun resultPartTwo(): Any {
        inputLines.forEach{println(it.decompressV2Length())}
        return ""
    }


    private fun String.decompress(): String {
        val result = StringBuilder()
        var i=0
        while (i < this.length) {
            val marker = this.checkMarker(i)
            if (marker != null) {
                val repeatingPart = this.substring(i+marker.length(), i+marker.length()+marker.first)
                result.append(repeatingPart.repeat(marker.second))
                i+= marker.length() + marker.first
            } else {
                result.append(this[i])
                i++
            }
        }
        return result.toString()
    }

    private fun String.decompressV2Length(): Long {
        var result = 0L
        var i=0
        while (i < this.length) {
            val marker = this.checkMarker(i)
            if (marker != null) {
                val repeatingPart = this.substring(i+marker.length(), i+marker.length()+marker.first)
                result += repeatingPart.decompressV2Length() * marker.second
                i+= marker.length() + marker.first
            } else {
                result++
                i++
            }
        }
        return result
    }


    private fun Pair<Int, Int>.length() =
        this.first.toString().length + this.second.toString().length + 3
}

fun String.checkMarker(startIndex: Int): Pair<Int, Int>? {
    if (this[startIndex] != '(')
        return null
    val endIndex = this.indexOf(')', startIndex)
    if (endIndex == -1)
        return null
    val marker = this.substring(startIndex+1, endIndex)
    val markerParts = marker.split("x")
    if (markerParts.size != 2)
        return null
    if (!markerParts[0].hasOnlyDigits() || !markerParts[1].hasOnlyDigits())
        return null
    return Pair(markerParts[0].toInt(), markerParts[1].toInt())
}


