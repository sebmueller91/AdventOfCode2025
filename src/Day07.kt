private const val DAY = 7

fun main() {
    fun part1(input: List<String>): Long {
        val grid = input.parse()

        var count = 0L
        for (i in 0..<grid.size-1) {
            count += grid.propagate1(i)
        }

        return count
    }

    fun part2(input: List<String>): Long {
        val grid = input.parse()
        val sColumn = grid.getColumnOfS()
        return grid.propagate2(0, sColumn)
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 21L)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()

    check(part2(testInput) == 40L)
    part2(input).println()
}

private fun Array<CharArray>.propagate1(row: Int): Long {
    var count = 0L
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

private fun Array<CharArray>.getColumnOfS(): Int = this[0].indexOfFirst { it == 'S' }

private val knownStates = HashMap<Pair<Int, Int>, Long>()
private fun Array<CharArray>.propagate2(x: Int, y: Int): Long {
    if (knownStates.containsKey(Pair(x,y))) {
        return knownStates.getValue(Pair(x,y))
    }
    if (x == this[y].size) {
        return 1
    }
    if (this[x+1][y] == '^') {
        val result = propagate2(x+1, y-1) + propagate2(x+1, y+1)
        knownStates[Pair(x,y)] = result
        return result
    }
    return propagate2(x+1,y)
}

private fun List<String>.parse(): Array<CharArray> = this.map { it.toCharArray() }.toTypedArray()

private fun Array<CharArray>.printGrid(iteration: Long) {
    "iteration: $iteration".println()
    for (i in this.indices) {
        for (j in this[i].indices) {
            this[i][j].print()
        }
        println()
    }
    println()
}