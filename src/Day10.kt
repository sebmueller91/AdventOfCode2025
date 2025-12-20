private const val DAY = 10

fun main() {
    fun part1(input: List<String>): Int {
        var requiredPresses = 0
        input.parse().forEach { machine ->
            requiredPresses += machine.matchOddity()
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

private fun Machine.matchOddity(): Int {
    val initialLightsState = CharArray(this.targetLightsState.length) { '.' }
    val initialButtonsPressed = buttons.map { false }
    return matchOddityRec(initialLightsState, initialButtonsPressed, 0)
}

private fun Machine.matchOddityRec(curLightsState: CharArray, pressedButtons: List<Boolean>, curButtonIndex: Int): Int {
    if (String(curLightsState) == targetLightsState) {
        return pressedButtons.count { it }
    }
    if (curButtonIndex == pressedButtons.size) {
        return Int.MAX_VALUE
    }
    var minResult = Int.MAX_VALUE
    val firstUnpressedIndex = pressedButtons.indexOfFirst { !it }


    listOf(true, false).forEach { pressButton ->
        val newLightsState = curLightsState.clone()
        val newPressedButtons = pressedButtons.toMutableList()
        if (pressButton) {
            newLightsState.press(buttons[curButtonIndex])
            newPressedButtons[curButtonIndex] = true
        }
        val result = matchOddityRec(newLightsState, newPressedButtons, curButtonIndex + 1)
        if (result < minResult) {
            minResult = result
        }

    }
    return minResult
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