package adventofcode2016

import tool.mylambdas.collectioncombination.mapCombinedItems

fun main() {
    Day11(test=false).showResult()
}

class Day11(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        val building = Building(
            if (test)
                listOf(
                    setOf(Chip("hydrogen"), Chip("lithium")),
                    setOf(Generator("hydrogen")),
                    setOf(Generator("lithium")),
                    setOf()
                )
            else
                listOf(
                    setOf(Generator("thulium"), Chip("thulium"), Generator("plutonium"), Generator("strontium")),
                    setOf(Chip("plutonium"), Chip("strontium"),),
                    setOf(Generator("qpromethium"), Chip("qpromethium"), Generator("ruthenium"), Chip("ruthenium"),),
                    setOf()
                )
        )
        return building.bfs()
    }

    override fun resultPartTwo(): Any {
        val building = Building(
            listOf(setOf(
                Generator("elerium"), Chip("elerium"),
                Generator("dilithium"), Chip("dilithium"),
                Generator("thulium"), Chip("thulium"),
                Generator("plutonium"), Generator("strontium")),
            setOf(Chip("plutonium"), Chip("strontium"),),
            setOf(Generator("qpromethium"), Chip("qpromethium"), Generator("ruthenium"), Chip("ruthenium"),),
            setOf()
            )
        )
        return building.bfs()
    }

    private fun Building.bfs(): Int {
        var count = 0L
        val deque = ArrayDeque<Pair<Building, Int>>().apply { this.addLast(Pair(this@bfs, 0)) }
        val alreadySeen = mutableSetOf<String>()

        while (deque.isNotEmpty()) {
            count++
            val (building, stepsDone) = deque.removeFirst()
            if (building.allOnTop()) {
                println("nodes visited: $count")
                return stepsDone
            }
            building
                .elevatorCandidates()
                .map { move -> building.doMove(move) }
                .filter { building -> building.hashKey() !in alreadySeen }.forEach { building ->
                    alreadySeen += building.hashKey()
                    deque.addLast(Pair(building, stepsDone + 1))
                }
        }
        println("nodes visited: $count")
        return -1
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

class Building(private val floors: List<Set<Component>>, private val elevatorPos: Int=0) {

    override fun toString(): String {
        return "E$elevatorPos | ${floorString(0)} | ${floorString(1)} | ${floorString(2)} | ${floorString(3)}"
    }

    fun hashKey(): String {
//        return "$elevatorPos|${floors.hashCode()}"
        val indexer = mutableMapOf<Char, Int>()
        var i = 1
        floors.forEach { floor ->
            floor.forEach { component ->
                if (component is Chip) {
                    indexer[component.name[0]] = i
                    i*=2
                }
            }
        }
        val maxI = i
        val floorMax = maxI * 8
        val sumList = floors.mapIndexed { floorIndex, floor ->
            floor.sumOf { component ->  indexer[component.name[0]]!! + if (component is Chip) 0 else maxI }
        }
        val total = sumList.joinToString("-")

//        var total = 0
//        floors.forEachIndexed { floorIndex, floor ->
//            total += floorMax * floorIndex +
//                    floor.sumOf { component -> indexer[component.name[0]]!! + if (component is Chip) 0 else maxI }
//        }

        return "$elevatorPos|$total"
    }

    private fun floorString(nr: Int) = floors[nr].sortedBy { it.name }.joinToString(",")

    fun doMove(move: Move): Building {
        return Building(
            floors = floors.mapIndexed { index, floor ->
                when (index) {
                    elevatorPos -> floor - move.components
                    elevatorPos + move.deltaFloor -> floor + move.components
                    else -> floor
                } },
            elevatorPos = elevatorPos + move.deltaFloor)
    }

    fun allOnTop() =
        floors[0].isEmpty() && floors[1].isEmpty() && floors[2].isEmpty()

    fun elevatorCandidates(): List<Move> {
        val currentFloor = floors[elevatorPos]
        val allCandidates =
            currentFloor.toList().mapCombinedItems { comp1, comp2 -> listOf(comp1, comp2) } +
                    currentFloor.map { comp -> listOf(comp) } //+ listOf(emptyList<Component>()) +

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

}