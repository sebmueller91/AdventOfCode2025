private const val DAY = 1

fun main() {
    fun part1(input: List<String>): Int {
        val instructions = input.parse()
        var countZeros = 0
        var curIndex = 50

        "The dial starts by pointing at $curIndex.".println()

        instructions.forEach { instruction ->
            curIndex += when (instruction.direction) {
                Direction.LEFT -> -instruction.steps
                Direction.RIGHT -> instruction.steps
            }
            curIndex = curIndex.ring(100)

            if (curIndex == 0) {
                countZeros++
            }

            "The dial is rotated ${instruction.direction}${instruction.steps} to point at $curIndex.".println()
        }

        return countZeros
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 3)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()
    part2(input).println()
}

private fun Int.ring(size: Int) = ((this % size) + size) % size

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