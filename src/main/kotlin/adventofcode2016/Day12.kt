package adventofcode2016

import java.lang.Exception

fun main() {
    Day12(test=false).showResult()
}

class Day12(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        val p = Program(programLines = inputLines)
        p.run()
        return p.getRegisterValues()['a']!!
    }

    override fun resultPartTwo(): Any {
        val p = Program(programLines = inputLines, registers = mutableMapOf('a' to 0, 'b' to 0, 'c' to 1, 'd' to 0))
        p.run()
        return p.getRegisterValues()['a']!!
    }

}

class Program(
    private val registers: MutableMap<Char, Long> = mutableMapOf('a' to 0, 'b' to 0, 'c' to 0, 'd' to 0),
    val programLines: List<String>) {

    fun run() {
        var ip = 0
        while (ip in programLines.indices) {
            val statement = programLines[ip]
            when(statement.operator()) {
                "cpy" -> {
                    registers[statement.operand2().asName()] = statement.operand1().asValue()
                    ip++
                }
                "inc" -> {
                    registers[statement.operand1().asName()] = registers[statement.operand1().asName()]!! + 1
                    ip++
                }
                "dec" -> {
                    registers[statement.operand1().asName()] = registers[statement.operand1().asName()]!! - 1
                    ip++
                }
                "jnz" -> {
                    ip += if (statement.operand1().asValue() != 0L) statement.operand2().asValue().toInt() else 1
                }
                else -> throw Exception("Unknown oprator")
            }
        }
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
        if (this.first() in registers.keys) this.first() else throw Exception("it is not a register")

    fun getRegisterValues() =
        registers.toMap()
}


