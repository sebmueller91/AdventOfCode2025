import kotlin.math.max
import kotlin.math.min

private const val DAY = 5

fun main() {
    fun part1(input: List<String>): Long {
        val (ranges, ids) = input.parse()
        return ids.count { id -> ranges.any { range -> id in range } }.toLong()
    }

    fun part2(input: List<String>): Long {
        val ranges = input.parse().first.toMutableList()

        while (ranges.mergeDuplicate()) { }

        return ranges.sumOf { (it.last - it.first) + 1 }
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 3L)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()

    check(part2(testInput) == 14L)
    part2(input).println()
}

private fun MutableList<LongRange>.mergeDuplicate(): Boolean {
    for (i in this.indices) {
        for (j in i+1 until this.size) {
            if (rangesOverlap(this[i], this[j])) {
                val newRange = mergeRanges(this[i], this[j])
                this.removeAt(j)
                this.removeAt(i)
                this.add(newRange)
                return true
            }
        }
    }
    return false
}

private fun List<String>.parse(): Pair<List<LongRange>, List<Long>> {
    val newLineIndex = this.indexOfFirst { it.isBlank() }
    val list1 = this.subList(0, newLineIndex)
    val list2 = this.subList(newLineIndex + 1, this.size)
    val ranges = list1.map { it.toLongRange() }
    val ids = list2.map { it.toLong() }
    return Pair(ranges, ids)
}

private fun String.toLongRange(): LongRange {
    val regex = Regex("""(\d+)-(\d+)""")
    val (start, end) = regex.find(this)!!.destructured
    return LongRange(start = start.toLong(), endInclusive = end.toLong())
}

private fun rangesOverlap(range1: LongRange, range2: LongRange): Boolean =
    (range1.first <= range2.first && range1.last >= range2.last) ||
            (range2.first <= range1.first && range2.last >= range1.last) ||
            range1.first in range2 || range1.last in range2 ||
            range2.first in range1 || range2.last in range1

private fun mergeRanges(range1: LongRange, range2: LongRange): LongRange =
    LongRange(min(range1.first, range2.first), endInclusive = max(range1.last, range2.last))