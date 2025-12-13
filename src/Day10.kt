private const val DAY = 10

fun main() {
    fun part1(input: List<String>): Int {
        var requiredPresses = 0
        input.parse().forEach { machine ->
            requiredPresses += machine.requiredPresses()
        }
        return requiredPresses
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 7)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()
    part2(input).println()
}

private fun Machine.requiredPresses(): Int {
    var curSearchDepth = 0
    while (true) {
        val initialLightsState = CharArray(this.targetLightsState.length) { '.' }
        if (requiredPressesRec(curLightsState = initialLightsState, curDepth = 0, maxDepth = curSearchDepth)) {
            return curSearchDepth
        }
        curSearchDepth++
    }
}

private fun Machine.requiredPressesRec(curLightsState: CharArray, curDepth: Int, maxDepth: Int): Boolean {
    if (String(curLightsState) == targetLightsState) {
        return true
    }
    if (curDepth >= maxDepth) {
        return false
    }
    buttons.forEach { button ->
        val newLightsState = curLightsState.clone()
        newLightsState.press(button)
        if (requiredPressesRec(newLightsState, curDepth + 1, maxDepth)) {
            return true
        }
    }
    return false
}

private fun CharArray.press(button: Button) {
    button.toggleLights.forEach { index ->
        this[index] =
            if (this[index] == '#') {
                '.'
            } else {
                '#'
            }
    }
}

private fun List<String>.parse(): List<Machine> {
    val list = mutableListOf<Machine>()
    val outerRegex = Regex("""\[(.*)] (.*) \{(.*)}""")
    val buttonsRegex = Regex("""\((.*)\)""")

    forEach { line ->
        val (part1, part2, part3) = outerRegex.find(line)!!.destructured
        val buttons = part2.split(' ')
        val buttonsList = mutableListOf<Button>()
        buttons.forEach { button ->
            val extracted = buttonsRegex.find(button)!!.destructured.match.groupValues[1]
            val toggleLights = extracted.split(',').map { it.toInt() }
            buttonsList.add(Button(toggleLights))
        }

        val joltages = part3.split(',').map { it.toInt() }
        list.add(
            Machine(
                targetLightsState = part1,
                buttons = buttonsList,
                joltages = joltages
            )
        )
    }

    return list
}

private class Machine(
    val targetLightsState: String,
    val buttons: List<Button>,
    val joltages: List<Int>
)

private data class Button(
    val toggleLights: List<Int>
)