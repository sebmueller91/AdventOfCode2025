package day1

import println
import readInput
import toDayString

private const val DAY = 1

fun main() {
    fun part1(input: List<String>): Int {
        val instructions = input.parse()
        var countZeros = 0
        var curIndex = 50

        instructions.forEach { instruction ->
            for (i in 1..instruction.steps) {
                curIndex += when (instruction.direction) {
                    Direction.LEFT -> -1
                    Direction.RIGHT -> +1
                }
                curIndex = curIndex.ring()
            }
            if (curIndex == 0) {
                countZeros++
            }
        }

        return countZeros
    }

    fun part2(input: List<String>): Int {
        val instructions = input.parse()
        var countZeros = 0
        var curIndex = 50

        instructions.forEach { instruction ->
            for (i in 1..instruction.steps) {
                curIndex += when (instruction.direction) {
                    Direction.LEFT -> -1
                    Direction.RIGHT -> +1
                }
                curIndex = curIndex.ring()
                if (curIndex == 0) {
                    countZeros++
                }
            }

        }

        return countZeros
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 3)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()

    part2(testInput).println()
    check(part2(testInput) == 6)

    part2(input).println()
}

private fun Int.ring() = ((this % 100) + 100) % 100

    private fun List<String>.parse(): List<Instruction> {
    var list = mutableListOf<Instruction>()
    val regex = Regex("""(L|R)(\d+)""")
    forEach { line ->
        val (directionString, stepsString) = regex.find(line)!!.destructured
        list.add(
            Instruction(
                direction = if (directionString == "L") Direction.LEFT else Direction.RIGHT,
                steps = stepsString.toInt()
            )
        )
    }
    return list
}

private data class Instruction(
    val direction: Direction,
    val steps: Int
)

private enum class Direction {
    LEFT,
    RIGHT
}