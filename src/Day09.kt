import java.awt.Polygon
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import kotlin.math.*

private const val DAY = 9

fun main() {
    fun part1(input: List<String>): Long {
        val coordinates = input.parse()
        var maxArea = Long.MIN_VALUE
        for (i in coordinates.indices) {
            for (j in coordinates.indices) {
                val rectangle = toRectangle(coordinates[i], coordinates[j])
                val area = ((rectangle.width + 1) * (rectangle.height + 1)).roundToLong()
                if (area > maxArea) {
                    maxArea = area
                }
            }
        }
        return maxArea
    }

    fun part2(input: List<String>): Long {
        val coordinates = input.parse()
        val polygon =
            Polygon(
                coordinates.map { it.x.toInt() }.toIntArray(),
                coordinates.map { it.y.toInt() }.toIntArray(),
                coordinates.size
            )

        var maxArea = Long.MIN_VALUE

        for (i in coordinates.indices) {
            for (j in coordinates.indices) {
                val rectangle = toRectangle(coordinates[i], coordinates[j])
                val area = ((rectangle.width + 1) * (rectangle.height + 1)).roundToLong()
                if (polygon.contains(rectangle) && area > maxArea) {
                    maxArea = area
                }
            }
        }
        return maxArea
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 50L)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()

    check(part2(testInput) == 24L)
    part2(input).println()
}

private fun toRectangle(p1: Point2D, p2: Point2D): Rectangle2D {
    val minX = min(p1.x, p2.x)
    val minY = min(p1.y, p2.y)
    val maxX = max(p1.x, p2.x)
    val maxY = max(p1.y, p2.y)
    return Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY)
}

private fun List<Point2D>.getElement(index: Int): Point2D = this[index % this.size]

private fun List<String>.parse(): List<Point2D> = map { line ->
    val (x, y) = Regex("""(\d+),(\d+)""").find(line)!!.destructured
    Point2D.Double(x.toDouble(), y.toDouble())
}