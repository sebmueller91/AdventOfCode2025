private const val DAY = 11

fun main() {
    fun part1(input: List<String>): Int {
        val youNode = input.parse().first { it.name == "you" }
        val outNode = input.parse().first { it.name == "out" }
        return countPaths(youNode, outNode)
    }

    fun part2(input: List<String>): Long {
        knownStates.clear()

        val svrNode = input.parse().first { it.name == "svr" }
        val outNode = input.parse().first { it.name == "out" }

        return countPaths(curNode = svrNode, targetNode = outNode, dacVisited = false, fftVisited = false)
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 5)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()

    val testInput2 = readInput("Day${DAY.toDayString()}_test2")
    check(part2(testInput2) == 2L)

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

private data class State(
    val dacVisited: Boolean,
    val fftVisited: Boolean,
    val curNodeName: String
)

private val knownStates = HashMap<State, Long>()
private fun countPaths(
    curNode: Node,
    targetNode: Node,
    dacVisited: Boolean,
    fftVisited: Boolean
): Long {
    val curState = State(
        dacVisited = dacVisited || curNode.name == "dac",
        fftVisited = fftVisited || curNode.name == "fft",
        curNodeName = curNode.name
    )
    if (knownStates.containsKey(curState)) {
        return knownStates.getValue(curState)
    }
    if (curNode.name == targetNode.name) {
        return if (curState.dacVisited && curState.fftVisited) 1 else 0
    }
    var count = 0L
    curNode.outputs.forEach { output ->
        count += countPaths(output, targetNode, curState.dacVisited, curState.fftVisited)
    }

    knownStates[curState] = count
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