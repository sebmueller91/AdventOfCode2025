import kotlin.math.abs
import kotlin.math.max

private const val DAY = 9

fun main() {
    fun part1(input: List<String>): Long {
        val coordinates = input.parse()
        var maxArea = Long.MIN_VALUE
        for (i in coordinates.indices) {
            for (j in coordinates.indices) {
                val area = calculateArea(coordinates[i], coordinates[j])
                if (area > maxArea) {
                    maxArea = area
                }
            }
        }
        return maxArea
    }

    fun part2(input: List<String>): Long {
        return input.size.toLong()
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 50L)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()
    part2(input).println()
}

private fun calculateArea(coordinate1: Coordinate, coordinate2: Coordinate): Long =
    (1+abs(coordinate1.x-coordinate2.x)).toLong() * (1+abs(coordinate1.y-coordinate2.y)).toLong()

private fun List<String>.parse(): List<Coordinate> = map { line ->
        val (x,y) = Regex("""(\d+),(\d+)""").find(line)!!.destructured
        Coordinate(x.toInt(), y.toInt())
    }

private data class Coordinate(
    val x: Int,
    val y: Int
)