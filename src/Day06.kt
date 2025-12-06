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

    fun part2(input: List<String>): Long {
        return input.solve2()
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 4277556L)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()

    check(part2(testInput) == 3263827L)
    part2(input).println()
}

private fun List<String>.parse(): Pair<Array<Array<Long>>, Array<Operator>> {
    val cleaned = this.map { it.split(' ').filter { it.isNotBlank() } }
    val numbers = cleaned.subList(0, cleaned.size - 1).map { it.map { it.toLong() }.toTypedArray() }.toTypedArray()
    val operators =  cleaned.last().map { it[0] }.map { it.toOperator() }.toTypedArray()
    return Pair(numbers, operators)
}

private fun List<String>.solve2(): Long{
    val cleaned = this.map { it.split(' ').filter { it.isNotBlank() } }
    val operators =  cleaned.last().map { it[0] }.map { it.toOperator() }.toTypedArray()

    val grid = this.map { it.toCharArray() }.toTypedArray()
    var count = 0L
    var curCount = 0L
    var operatorIndex = 0
    val maxColumns = grid.maxOf { it.size }
    for (i in 0..<maxColumns) {
        var numberString = ""

        for (j in 0..<grid.size-1) {
            if (i > grid[j].size-1) {
                continue
            }
            if (grid[j][i] == ' ') {
                continue
            }
            numberString += grid[j][i]
        }
        if (numberString.isBlank()) {
            operatorIndex++
            count += curCount
            curCount = 0
            continue
        }
        val number = numberString.toLong()
        if (curCount == 0L) {
            curCount = number
        } else {
            when (operators[operatorIndex]) {
                Operator.PLUS -> {
                    curCount += number
                }
                Operator.MULTIPLY -> {
                    curCount *= number
                }
            }
        }
    }
    count += curCount

    return count
}

private fun Char.toOperator(): Operator = if (this == '+') Operator.PLUS else Operator.MULTIPLY

private enum class Operator {
    PLUS, MULTIPLY
}