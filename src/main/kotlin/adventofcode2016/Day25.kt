package adventofcode2016

import java.lang.Exception

fun main() {
    Day25(test=false).showResult()
}

class Day25(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        return generateSequence(0L){it+1}.first { initA ->
            val p = ProgramDay25(programLines = inputLines, registers = mutableMapOf('a' to initA, 'b' to 0, 'c' to 0, 'd' to 0))
            p.run()
        }
    }
}


class ProgramDay25(
    private val registers: MutableMap<Char, Long> = mutableMapOf('a' to 0, 'b' to 0, 'c' to 0, 'd' to 0),
    val programLines: List<String>) {

    fun run(): Boolean {
        var countRepeaters = 0
        var previous = -1L

        val overrides = mutableMapOf<Int, String>()
        var ip = 0
        while (ip in programLines.indices) {
            val statement = overrides.getOrDefault(ip, programLines[ip])
            when (statement.operator()) {
                "cpy" -> {
                    if (statement.operand2().asName() != null)
                        registers[statement.operand2().asName()!!] = statement.operand1().asValue()
                    ip++
                }

                "inc" -> {
                    if (statement.operand1().asName() != null)
                        registers[statement.operand1().asName()!!] = registers[statement.operand1().asName()]!! + 1
                    ip++
                }

                "dec" -> {
                    if (statement.operand1().asName() != null)
                        registers[statement.operand1().asName()!!] = registers[statement.operand1().asName()]!! - 1
                    ip++
                }

                "jnz" -> {
                    ip += if (statement.operand1().asValue() != 0L) statement.operand2().asValue().toInt() else 1
                }

                "out" -> {
                    if (previous == statement.operand1().asValue()) {
                        ip = programLines.size+100
                    } else {
                        if (countRepeaters++ >= 1000) {
                            return true
                        }
                        previous = statement.operand1().asValue()
                        ip++
                    }
                }
                else -> throw Exception("Unknown operator")
            }
        }
        return false
    }

    private fun String.operator() =
        this.substringBefore(" ")

    private fun String.operand1() =
        this.substringAfter(" ").substringBefore(" ")

    private fun String.operand2() =
        this.substringAfterLast(" ")

    private fun String.asValue() =
        if (this.first() in registers.keys) registers[this.first()]!! else this.toLong()

    private fun String.asName() =
        if (this.first() in registers.keys) this.first() else null

    fun getRegisterValues() =
        registers.toMap()

}