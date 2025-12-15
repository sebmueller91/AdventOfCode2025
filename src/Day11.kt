private const val DAY = 11

fun main() {
    fun part1(input: List<String>): Int {
        val youNode = input.parse().first { it.name == "you" }
        val outNode = input.parse().first { it.name == "out" }
        return countPaths(youNode, outNode)
    }

    fun part2(input: List<String>): Int {
        val svrNode = input.parse().first { it.name == "svr" }
        val fftNode = input.parse().first { it.name == "fft" }
        val dacNode = input.parse().first { it.name == "dac" }
        val outNode = input.parse().first { it.name == "out" }

        "1".println()
        val svrToFft = countPaths(svrNode, fftNode)
        "2".println()
        val svrToDac = countPaths(svrNode, dacNode)
        "3".println()
        val dacToFft = countPaths(dacNode, fftNode)
        "4".println()
        val fftToDac = countPaths(fftNode, dacNode)
        "5".println()
        val fftToOut = countPaths(fftNode, outNode)
        "6".println()
        val dacToOut = countPaths(dacNode, outNode)
        "7".println()

        return svrToFft*fftToDac*dacToOut + svrToDac*dacToFft*fftToOut
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 5)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()

    val testInput2 = readInput("Day${DAY.toDayString()}_test2")
    check(part2(testInput2) == 2)

    part2(input).println()
}

private fun countPaths(curNode: Node, targetNode: Node, traversed: List<Node> = listOf()): Int {
    if (traversed.contains(curNode)) {
        return 0
    }
    if (curNode.name == targetNode.name) {
        return 1
    }
    var count = 0
    curNode.outputs.forEach { output ->
        val newTraversed = traversed.toMutableList().apply { add(curNode) }
        count += countPaths(output, targetNode, newTraversed)
    }
    return count
}

private fun countPaths2(curNode: Node, traversed: List<Node>): Int {
    if (curNode.name == "out") {
        val correctPath = traversed.any { it.name == "dac" } && traversed.any { it.name == "fft" }
        return if (correctPath) 1 else 0
    }
    var count = 0
    curNode.outputs.forEach { output ->
        val newTraversed = traversed.toMutableList().apply { add(curNode) }
        count += countPaths2(output, newTraversed)
    }
    return count
}

private fun List<String>.parse(): List<Node> {
    val nodes = mutableListOf<Node>()
    val regex = Regex("""(.*): (.*)""")

    forEach { line ->
        val (nodeString, _) = regex.find(line)!!.destructured
        nodes.add(Node(name = nodeString, outputs = mutableListOf()))
    }
    nodes.add(Node(name = "out", outputs = mutableListOf()))
    forEachIndexed { index, line ->
        val (_, outputsString) = regex.find(line)!!.destructured
        val outputs = outputsString.split(' ')
        outputs.forEach { output ->
            val outputNode = nodes.first { it.name == output }
            nodes[index].outputs.add(outputNode)
        }
    }

    return nodes
}

private class Node(
    val name: String,
    val outputs: MutableList<Node>
)