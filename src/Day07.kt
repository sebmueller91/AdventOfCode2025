private const val DAY = 7

fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.parse()

        var count = 0
//        grid.printGrid(-1)
        for (i in 0..<grid.size-1) {
            count += grid.propagate(i)
//            grid.printGrid(i)
        }

        return count
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 21)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()
    part2(input).println()
}

private fun Array<CharArray>.propagate(row: Int): Int {
    var count = 0
    for (j in 0..<this[row].size) {
        if (this[row][j] != 'S' && this[row][j] != '|') {
            continue
        }

        if (this[row+1][j] == '^') {
            count++
            this[row+1][j+-1] = '|'
            this[row+1][j+1] = '|'
        } else {
            this[row+1][j] = '|'
        }
    }

    return count
}

private fun List<String>.parse(): Array<CharArray> = this.map { it.toCharArray() }.toTypedArray()

private fun Array<CharArray>.printGrid(iteration: Int) {
    "iteration: $iteration".println()
    for (i in this.indices) {
        for (j in this[i].indices) {
            this[i][j].print()
        }
        println()
    }
    println()
}