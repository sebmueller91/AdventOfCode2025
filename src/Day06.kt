private const val DAY = 6

fun main() {
    fun part1(input: List<String>): Long {
        val (numbers, operators) = input.parse()
        var count = 0L
        for (i in numbers[0].indices) {
            var columnCount = numbers[0][i]
            for (j in 1..<numbers.size) {
                when (operators[i]) {
                    Operator.PLUS -> {
                        columnCount += numbers[j][i]
                    }
                    Operator.MULTIPLY -> {
                        columnCount *= numbers[j][i]
                    }
                }
            }
            count += columnCount
        }

        return count
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 4277556L)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.parse(): Pair<Array<Array<Long>>, Array<Operator>> {
    val cleaned = this.map { it.split(' ').filter { it.isNotBlank() } }
    val numbers = cleaned.subList(0, cleaned.size - 1).map { it.map { it.toLong() }.toTypedArray() }.toTypedArray()
    val operators =  cleaned.last().map { it[0] }.map { it.toOperator() }.toTypedArray()
    return Pair(numbers, operators)
}

private fun Char.toOperator(): Operator = if (this == '+') Operator.PLUS else Operator.MULTIPLY

private enum class Operator {
    PLUS, MULTIPLY
}