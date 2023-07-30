package adventofcode2016

import tool.mylambdas.collectioncombination.mapCombinedItems
import kotlin.math.min

fun main() {
    Day11(test=true).showResult()
}

class Day11(test: Boolean) : PuzzleSolverAbstract(test) {

    private val building = Building(test)

    override fun resultPartOne(): Any {
        val x = solve()
        println("nodes visited: $count")
        return x
    }

    private val hash = mutableMapOf<String, Int>()
    private var count = 0L
    private fun solve(alreadySeen: Set<String> = emptySet(), shortestSoFar: Int = 999_999_999 ): Int {
        count++
        if (building.allOnTop()) {
            return 0
        }

        if (hash.contains(building.hashKey()) ) {
            return hash[building.hashKey()]!!
        }

        if (building.minimalStepsToTop() >= shortestSoFar) {
            return 999_999_999
        }

        val visited = alreadySeen + building.hashKey()
        var shortest = 999_999_999
        val allMoves = building.elevatorCandidates()
        allMoves.forEach { move ->
            building.doMove(move)
            if (building.hashKey() !in visited) {
                val tmp = solve(visited, min (shortest, shortestSoFar))
                shortest = min(shortest,tmp)
            }
            building.undoMove(move)
        }
        if (shortest < 10000)
            hash[building.hashKey()] = shortest+1
        return shortest+1
    }
}

abstract class Component(open val name: String)
data class Chip(override val name: String): Component(name) {
    override fun toString() = "${name[0].uppercase()}M"
}

data class Generator(override val name: String): Component(name) {
    override fun toString() = "${name[0].uppercase()}G"
}

data class Move(val deltaFloor: Int, val components: List<Component>)

//The first floor contains a thulium generator, a thulium-compatible microchip, a plutonium generator, and a strontium generator.
//The second floor contains a plutonium-compatible microchip and a strontium-compatible microchip.
//The third floor contains a promethium generator, a promethium-compatible microchip, a ruthenium generator, and a ruthenium-compatible microchip.
//The fourth floor contains nothing relevant.

class Building(test: Boolean) {
    private val floors: List<MutableSet<Component>> = if (test) {
        listOf(
            mutableSetOf(Chip("hydrogen"), Chip("lithium")),
            mutableSetOf(Generator("hydrogen")),
            mutableSetOf(Generator("lithium")),
            mutableSetOf()
        )
    } else {
        listOf(
            mutableSetOf(Generator("thulium"), Chip("thulium"), Generator("plutonium"), Generator("strontium")),
            mutableSetOf(Chip("plutonium"),Chip("strontium"),),
            mutableSetOf(Generator("qpromethium"),Chip("qpromethium"),Generator("ruthenium"),Chip("ruthenium"),),
            mutableSetOf()
        )
    }

    private var elevatorPos = 0

    override fun toString(): String {
        return "E$elevatorPos | ${floorString(0)} | ${floorString(1)} | ${floorString(2)} | ${floorString(3)}"
    }

    fun hashKey(): String {
        return "$elevatorPos|${floors.hashCode()}"
    }

    override fun hashCode(): Int {
        return "$elevatorPos|${floors.hashCode()}".hashCode()
    }


    private fun floorString(nr: Int) = floors[nr].sortedBy { it.name }.joinToString(",")

    fun doMove(move: Move) {
        floors[elevatorPos] -= move.components
        elevatorPos += move.deltaFloor
        floors[elevatorPos] += move.components
    }

    fun undoMove(move: Move) {
        floors[elevatorPos] -= move.components
        elevatorPos -= move.deltaFloor
        floors[elevatorPos] += move.components
    }

    fun allOnTop() =
        floors[3].size == 4

    fun elevatorCandidates(): List<Move> {
        val currentFloor = floors[elevatorPos]
        val allCandidates = currentFloor.map { comp -> listOf(comp) } + //listOf(emptyList<Component>()) +
                currentFloor.toList().mapCombinedItems { comp1, comp2 -> listOf(comp1, comp2) }

        return (-1..1 step 2).filter { elevatorPos+it in 0..3 }
            .flatMap{ deltaPos -> allCandidates.validForFloor(floors[elevatorPos+deltaPos]).map{validCandidate -> Move(deltaPos, validCandidate) }}
    }

    private fun List<List<Component>>.validForFloor(floor: Set<Component>) =
        this.filter { candidate -> (floor + candidate).isSecure() }

    private fun Set<Component>.isSecure() =
        this.generatorNames().isEmpty() || this.chipNames().all {chipName -> chipName in this.generatorNames()}

    private fun Set<Component>.chipNames() =
        this.filterIsInstance<Chip>().map{it.name}.toSet()

    private fun Set<Component>.generatorNames() =
        this.filterIsInstance<Generator>().map{it.name}.toSet()

    fun minimalStepsToTop() =
        4 - elevatorPos

}