import kotlin.math.abs

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
        val coordinates = input.parse()
        val (horizontalLines, verticalLines) = coordinates.getLines()

        return input.size.toLong()
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 50L)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()

    check(part2(testInput) == 24L)
    part2(input).println()
}

private fun calculateArea(coordinate1: Coordinate, coordinate2: Coordinate): Long =
    (1 + abs(coordinate1.x - coordinate2.x)).toLong() * (1 + abs(coordinate1.y - coordinate2.y)).toLong()

private fun getLinePairs().

private fun List<Coordinate>.getLines(): Pair<List<Line>, List<Line>> {
    val horizontalLines = mutableListOf<Line>()
    val verticalLines = mutableListOf<Line>()
    for (i in indices) {
        val p1 = this.getElement(i)
        val p2 = this.getElement(i + 1)
        if (p1.x == p2.x) {
            horizontalLines.add(Line(p1, p2))
        } else {
            verticalLines.add(Line(p1, p2))
        }
    }
    return Pair(horizontalLines, verticalLines)
}


private fun List<Line>.getLineParis(): List<Pair<Line, Line>> {
    indices.flatMap { i ->
        indices.sli
    }
}


private fun orientation(p1: Coordinate, p2: Coordinate, p3: Coordinate): Int =
    (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x)

private fun List<Coordinate>.getElement(index: Int): Coordinate = this[index % this.size]

private fun List<String>.parse(): List<Coordinate> = map { line ->
    val (x, y) = Regex("""(\d+),(\d+)""").find(line)!!.destructured
    Coordinate(x.toInt(), y.toInt())
}

private data class Rectangle(
    val h1: Line,
    val h2: Line,
    val v1: Line,
    val v2: Line,
)

private data class Line(
    val p1: Coordinate,
    val p2: Coordinate,
) {
    fun intersects(otherLine: Line): Boolean =
        (orientation(p1, p2, otherLine.p1) * orientation(p1, p2, otherLine.p2) <= 0) &&
                (orientation(otherLine.p1, otherLine.p2, p1) * orientation(otherLine.p1, otherLine.p2, p2) <= 0)
}

private data class Coordinate(
    val x: Int,
    val y: Int
)