import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class Day08 {
}

private const val DAY = 8

fun main() {
    fun part1(input: List<String>, iterations: Int): Long {
        val junctions = input.parse()
        val distances = junctions.calculateDistances()
        val circuits  = junctions.indices.toList().map { mutableSetOf(it) }.toMutableList()

        repeat(iterations) {
            val minIndex = distances.getCoordinatesOfMin()
            circuits.mergeJunctions(minIndex.first, minIndex.second, distances)
        }

        return circuits.sortedByDescending { it.size }.take(3).map { it.size }.reduce {acc, i -> acc*i}.toLong()
    }

    fun part2(input: List<String>): Long {
        val junctions = input.parse()
        val distances = junctions.calculateDistances()
        val circuits  = junctions.indices.toList().map { mutableSetOf(it) }.toMutableList()

        while (true) {
            val minIndex = distances.getCoordinatesOfMin()
            circuits.mergeJunctions(minIndex.first, minIndex.second, distances)
            if (circuits.size == 1) {
                return junctions[minIndex.first].x * junctions[minIndex.second].x
            }
        }
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput, 10) == 40L)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input, 1000).println()

    check(part2(testInput) == 25272L)
    part2(input).println()
}

private fun MutableList<MutableSet<Int>>.mergeJunctions(junctionIndex1: Int, junctionIndex2: Int, distances: Array<Array<Double>>) {
    val circuitIndex1 = indexOfFirst { it.contains(junctionIndex1) }
    val circuitIndex2 = indexOfFirst { it.contains(junctionIndex2) }
    if (circuitIndex1 == circuitIndex2) {
        distances[junctionIndex1][junctionIndex2] = Double.MAX_VALUE
        distances[junctionIndex2][junctionIndex1] = Double.MAX_VALUE
        return
    }
    this[circuitIndex1].addAll(this[circuitIndex2])
    this.removeAt(circuitIndex2)
    return
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