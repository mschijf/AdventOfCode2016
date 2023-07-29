package adventofcode2016

fun main() {
    Day10(test=false).showResult()
}

class Day10(test: Boolean) : PuzzleSolverAbstract(test) {

    private val chipHolders = createChipHolders()

    override fun resultPartOne(): Any {
        val checkValues = if (test) Pair(2,5) else Pair(17,61)
        while (chipHolders.values.any { it.hasTwo() }) {
            val proceedingBot = chipHolders.values.first {it.hasTwo()}
            if (proceedingBot.proceed(allChipHolders = chipHolders) == checkValues)
                return "The bot comparing $checkValues is: ${proceedingBot.name}"
        }
        return "none of the bots compared $checkValues"
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
            throw Exception("Too much chips in holder")
        chips.add(chip)
    }

    fun proceed(allChipHolders: Map<String, ChipHolder>): Pair<Int, Int> {
        if (chips.size != 2)
            throw Exception("not exactly two item in holder!")

        if (giveLowTo == null || giveHighTo == null) {
            throw Exception("no two!")
        } else {
            val compare = Pair(chips.min(), chips.max())
            chips.clear()
            allChipHolders[giveLowTo]!!.addChip(compare.first)
            allChipHolders[giveHighTo]!!.addChip(compare.second)
            return compare
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

