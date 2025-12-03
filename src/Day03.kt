private const val DAY = 3

fun main() {
    fun part1(input: List<String>): Int {
        return input.parse().sumOf { it.highestJoltage() }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 357)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()
    part2(input).println()
}

private fun List<Int>.highestJoltage(): Int {
    val firstDigit = this.subList(0, this.size - 1).maxOf { it }
    val indexOfFirstDigit = this.indexOfFirst { firstDigit == it }
    val remainingList = this.subList(indexOfFirstDigit + 1, this.size)
    val secondDigit = remainingList.maxOf { it }
    return firstDigit * 10 + secondDigit
}

private fun List<String>.parse(): List<List<Int>> = this.map { it.toList().map { it.digitToInt() } }