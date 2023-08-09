package adventofcode2016

import tool.math.fac
import java.lang.Exception

fun main() {
    Day23(test=false).showResult()
}

class Day23(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        val p = ProgramDay23(programLines = inputLines, registers = mutableMapOf('a' to 7, 'b' to 0, 'c' to 0, 'd' to 0))
        p.run()
        return p.getRegisterValues()['a']!!
    }

    /**
     * blijft hangen. zal de code moeten uitppluizen en vertalen naar een gewoon programma.
     * Ik zie geen andre oplossing dan dit ergel voor regl te bekijken en te vertalen
     * KOmt er uiteindelijk op neer dat eerst de faculteit van 'a' wordt berekend
     * en dan in een tweede stuk komt daar nog 79*74 bij
     *
     * Dus met 12 als startwaarde voor a, wordt dat: 12!+74*79 = 479.007.446
     *
     */
    override fun resultPartTwo(): Any {
        return 12.fac() + 74*79

//        val p = ProgramDay23(programLines = inputLines, registers = mutableMapOf('a' to 12, 'b' to 0, 'c' to 0, 'd' to 0))
//        p.run()
//        return p.getRegisterValues()['a']!!
    }

}

class ProgramDay23(
    private val registers: MutableMap<Char, Long> = mutableMapOf('a' to 0, 'b' to 0, 'c' to 0, 'd' to 0),
    val programLines: List<String>) {

    fun run() {
        val overrides = mutableMapOf<Int, String>()
        var ip = 0
        while (ip in programLines.indices) {
            val statement = overrides.getOrDefault(ip, programLines[ip])
            when(statement.operator()) {
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
                "tgl" -> {
                    val changeIndex = (ip + statement.operand1().asValue()).toInt()
                    if (changeIndex in programLines.indices) {
                        when (val stat = programLines[changeIndex].operator()) {
                            "inc" -> overrides[changeIndex] = programLines[changeIndex].replace(stat, "dec")
                            "dec", "tgl" -> overrides[changeIndex] = programLines[changeIndex].replace(stat, "inc")
                            "jnz" -> overrides[changeIndex] = programLines[changeIndex].replace(stat, "cpy")
                            "cpy" -> overrides[changeIndex] = programLines[changeIndex].replace(stat, "jnz")
                            else -> throw Exception("Unknown operator")
                        }
                    }
                    ip++
                }
                else -> throw Exception("Unknown operator")
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
        if (this.first() in registers.keys) this.first() else null

    fun getRegisterValues() =
        registers.toMap()
}


