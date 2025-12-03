private const val DAY = 3

fun main() {
    fun part1(input: List<String>): Long {
        return input.parse().sumOf { it.highestJoltage() }
    }

    fun part2(input: List<String>): Long {
        return input.parse().sumOf { it.highestJoltage(12) }
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 357L)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()

    check(part2(testInput) == 17524L)
    part2(input).println()
}

private fun List<Int>.highestJoltage(numberDigits: Int = 2): Long {
    val extractedDigits = mutableListOf<Int>()
    var curList = this

    for (i in numberDigits-1 downTo 0) {
        extractedDigits.add(curList.subList(0, curList.size - i).maxOf { it })

        val indexOfFirstDigit = curList.indexOfFirst { extractedDigits.first() == it }
        curList = curList.subList(indexOfFirstDigit + 1, curList.size)
    }

    return extractedDigits.joinToString ("").toLong()
}

private fun List<String>.parse(): List<List<Int>> = this.map { it.toList().map { it.digitToInt() } }