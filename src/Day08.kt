import kotlin.math.pow
import kotlin.math.sqrt

class Day08 {
}

private const val DAY = 8

fun main() {
    fun part1(input: List<String>, iterations: Int): Long {
        val junctions = input.parse()
        val distances = junctions.calculateDistances()
        val circuits = mutableListOf<MutableSet<Junction>>()

        repeat(iterations) {
            val minIndex = distances.getCoordinatesOfMin()
            circuits.addJunctions(junctions[minIndex.first], junctions[minIndex.second])
            circuits.sortedByDescending { it.size }.map { it.size }.println()
        }

        return circuits.sortedByDescending { it.size }.take(3).map { it.size }.reduce {acc, i -> acc*i}.toLong()
    }

    fun part2(input: List<String>): Long {
        return input.size.toLong()
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput, 10) == 40L)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input, 1000).println()
    part2(input).println()
}

private fun MutableList<MutableSet<Junction>>.addJunctions(junction1: Junction, junction2: Junction) {
    forEach { circuit ->
        if (circuit.contains(junction1) || circuit.contains(junction2)) {
            circuit.add(junction1)
            circuit.add(junction2)
            return
        }
    }
    add(mutableSetOf(junction1, junction2))
}

private fun Array<Array<Double>>.getCoordinatesOfMin(): Pair<Int, Int> {
    var index = Pair(0, 0)
    var value = this[index.first][index.second]

    for (i in this.indices) {
        for (j in this.indices) {
            if (this[i][j] < value) {
                index = Pair(i, j)
                value = this[i][j]
            }
        }
    }

    this[index.first][index.second] = Double.MAX_VALUE
    this[index.second][index.first] = Double.MAX_VALUE
    return index
}

private fun List<Junction>.calculateDistances(): Array<Array<Double>> {
    val distances = Array(this.size) { Array(this.size) { Double.MAX_VALUE } }
    for (i in this.indices) {
        for (j in i + 1..<this.size) {
            if (i == j) {
                continue
            }
            distances[i][j] = this[i].distanceTo(this[j])
        }
    }
    return distances
}

private fun List<String>.parse(): List<Junction> {
    val list = mutableListOf<Junction>()
    val regex = Regex("""(\d+),(\d+),(\d+)""")
    forEach { line ->
        val (x, y, z) = regex.find(line)!!.destructured
        list.add(Junction(x.toLong(), y.toLong(), z.toLong()))
    }
    return list
}

private data class Junction(
    val x: Long,
    val y: Long,
    val z: Long
) {
    fun distanceTo(other: Junction): Double =
        sqrt((x - other.x).toDouble().pow(2) + (y - other.y).toDouble().pow(2) + (z - other.z).toDouble().pow(2))

}