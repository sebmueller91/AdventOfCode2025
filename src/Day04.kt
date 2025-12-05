private const val DAY = 4

fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.parse()
        var accessibleCount = 0

        for (i in grid.indices) {
            for (j in 0..<grid[0].size) {
                if (grid.isAccessible(i,j)) {
                    accessibleCount++
                }
            }
        }

        return accessibleCount
    }

    fun part2(input: List<String>): Int {
        val grid = input.parse()
        var changed = true
        var accessibleCount = 0

        while (changed) {
            changed = false
            for (i in grid.indices) {
                for (j in 0..<grid[0].size) {
                    if (grid.isAccessible(i,j)) {
                        accessibleCount++
                        grid[i][j] = '.'
                        changed = true
                    }
                }
            }
        }

        return accessibleCount
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 13)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()

    check(part2(testInput) == 43)
    part2(input).println()
}

private fun Array<CharArray>.isAccessible(i: Int, j: Int): Boolean {
    if (this[i][j] == '.') {
        return false
    }
    var paperNeighbourCount = 0
    for (ii in -1..1) {
        for (jj in -1..1) {
            val x = i + ii
            val y = j + jj
            if (x !in this.indices) {
                continue
            }
            if (y !in 0..<this[0].size) {
                continue
            }
            if (x == i && y == j) {
                continue
            }
            if (this[x][y] == '.') {
                continue
            }
            paperNeighbourCount++
        }
    }
    return paperNeighbourCount < 4
}

private fun List<String>.parse(): Array<CharArray> = this.map { it.toCharArray() }.toTypedArray()