private const val DAY = 2

fun main() {
    fun part1(input: List<String>): Long {
        val ranges = input.parse()
        return ranges.map { range -> range.filter { it.isInvalid1() } }.sumOf { it.sum() }
    }

    fun part2(input: List<String>): Long {
        val ranges = input.parse()
        return ranges.map { range -> range.filter { it.isInvalid2() } }.sumOf { it.sum() }
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 1227775554L)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()

    check(part2(testInput) == 4174379265L)
    part2(input).println()
}

private fun Long.isInvalid1(): Boolean =
    this.toString().isInvalid1()

private fun String.isInvalid1(): Boolean =
    this.substring(0, this.length / 2) == this.substring(this.length / 2, this.length)

private fun Long.isInvalid2(): Boolean =
    this.toString().isInvalid2()

private fun String.isInvalid2(): Boolean {
    for (i in 1..this.length/2) {
        val segments = this.chunked(i)
        if (segments.distinct().size == 1) {
            return true
        }
    }
    return false
}

private fun List<String>.parse(): List<LongRange> =
    this[0].split(',').map { range -> range.split('-') }
        .map { LongRange(start = it[0].toLong(), endInclusive = it[1].toLong()) }

private data class Range(
    val start: String,
    val end: String
)