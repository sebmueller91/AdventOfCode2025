private const val DAY = 12

fun main() {
    fun part1(input: List<String>): Int {
        val (presents, regions) = input.parse()
        var count = 0
        regions.forEach { region ->
            var curSizeLeft = region.height * region.width
            region.presentNumbers.forEachIndexed { presentIndex, number ->
                curSizeLeft -=  presents[presentIndex].size() * number
            }
            if (curSizeLeft >= 0) {
                count++
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
//    check(part1(testInput) == 2)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.parse(): Pair<List<Present>, List<Region>> {
    val presentIndexRegex = Regex("""^(\d+):""")
    val regionRegex = Regex("""^(\d+)x(\d+): (.*)""")

    val subLists = mutableListOf<List<String>>()
    var curSubList = mutableListOf<String>()
    this.forEach { line ->
        if (line.isBlank()) {
            subLists.add(curSubList.toList())
            curSubList.clear()
        } else {
            curSubList.add(line)
        }
    }
    subLists.add(curSubList.toList())

    val presents = mutableListOf<Present>()
    subLists.subList(0, subLists.size - 1).forEach { lines ->
        presents.add(
            Present(
                id = presentIndexRegex.find(lines.first())!!.destructured.match.groupValues[1].toInt(),
                shape = lines.subList(1, lines.size).map { it.map { it == '#' }.toTypedArray() }.toTypedArray()
            )
        )
    }

    val regions = mutableListOf<Region>()
    subLists.last().forEach {
        val (width, height, presentNumbers) = regionRegex.find(it)!!.destructured
        regions.add(
            Region(
                width = width.toInt(),
                height = height.toInt(),
                presentNumbers = presentNumbers.split(' ').map { it.toInt() }.toTypedArray()
            )
        )
    }
    return Pair(presents, regions)
}

private class Present(
    val id: Int,
    val shape: Array<Array<Boolean>>
) {
    fun size(): Int = shape.sumOf { it.sumOf { if (it) 1 else 0 } }
}

private class Region(
    val width: Int,
    val height: Int,
    val presentNumbers: Array<Int>
)