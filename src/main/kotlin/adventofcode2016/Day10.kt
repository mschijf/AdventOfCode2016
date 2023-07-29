package adventofcode2016

fun main() {
    Day10(test=false).showResult()
}

class Day10(test: Boolean) : PuzzleSolverAbstract(test) {

    private val chipHolders = createChipHolders()

    override fun resultPartOne(): Any {
        val checkValues = if (test) Pair(2,5) else Pair(17,61)
//        println(chipHolders.values.filter { it.hasTwo() }.count())
        while (chipHolders.values.any { it.hasTwo() }) {
            val proceedingBot = chipHolders.values.first {it.hasTwo()}
            val valuesCompared = proceedingBot.comparing()
            if (valuesCompared == checkValues)
                println("The comparing bot is: ${proceedingBot.name}")
            proceedingBot.proceed(allChipHolders = chipHolders)
//            println(chipHolders.values.count { it.hasTwo() })
        }
        return ""
    }

    override fun resultPartTwo(): Any {
        while (chipHolders.values.any { it.hasTwo() }) {
            chipHolders.values.first {it.hasTwo()}.proceed(allChipHolders = chipHolders)
        }
        return chipHolders["output 0"]!!.multipliedValue() * chipHolders["output 1"]!!.multipliedValue() * chipHolders["output 2"]!!.multipliedValue()
    }

    private fun createChipHolders(): Map<String, ChipHolder> {
        val result = inputLines.filter{it.startsWith("bot")}.map{ChipHolder.of(it)}.associateBy{it.name}.toMutableMap()
        inputLines
            .filter { it.startsWith("value") }
            .forEach {
                val chipHolderName = it.substringAfter("goes to ")
                result.getOrPut(chipHolderName) { ChipHolder(chipHolderName) }
                    .addChip(it.substringAfter("value ").substringBefore(" goes").toInt())
            }
        result.values.filterNot { it.giveLowTo == null || result.contains(it.giveLowTo) }.forEach {
            result[it.giveLowTo!!] = ChipHolder(it.giveLowTo)
//            println("low: '${it.giveLowTo}'")
        }
        result.values.filterNot { it.giveHighTo == null || result.contains(it.giveHighTo) }.forEach {
            result[it.giveHighTo!!] = ChipHolder(it.giveHighTo)
//            println("high: '${it.giveHighTo}'")
        }
        return result
    }
}


data class ChipHolder(val name: String, val giveLowTo: String?=null, val giveHighTo:String?=null) {
    private val chips = mutableListOf<Int>()

    fun hasTwo() =
        chips.size == 2

    fun addChip(chip: Int) {
        if (chips.count() >= 2)
            println("TOOOOOO much")
        chips.add(chip)
    }

    fun comparing() =
        Pair(chips.min(), chips.max())

    fun proceed(allChipHolders: Map<String, ChipHolder>) {
        if (chips.size != 2)
            throw Exception("no two!")
        if (giveLowTo == null || giveHighTo == null) {
            throw Exception("no two!")
        } else {
            allChipHolders[giveLowTo]!!.addChip(chips.min())
            allChipHolders[giveHighTo]!!.addChip(chips.max())
            chips.clear()
        }
    }

    fun multipliedValue() =
        chips.reduce{ acc, elt -> acc*elt }

    companion object {
        fun of(raw: String): ChipHolder {
            return ChipHolder(
                name = raw.substringBefore(" gives"),
                giveLowTo = raw.substringAfter("gives low to ").substringBefore(" and"),
                giveHighTo = raw.substringAfter("high to ")
            )
        }
    }
}

