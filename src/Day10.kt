private const val DAY = 10

fun main() {
    fun part1(input: List<String>): Int {
        var requiredPresses = 0
        input.parse().forEach { machine ->
            val successfulCombinations = machine.matchOddity(machine.targetLightsState)
            requiredPresses += successfulCombinations.minOf { it.count { it } }
        }
        return requiredPresses
    }

    fun part2(input: List<String>): Int {
        var requiredPresses = 0
//        input.parse().forEachIndexed { index, machine ->
////            index.println()
//            val oddities = machine.targetJoltages.calculateOddities()
//            val (requiredPressesForOddities, joltages) = machine.matchOddity(oddities)
//            joltages.toList().println()
//            requiredPresses += requiredPressesForOddities
//            requiredPresses += machine.matchJoltages(joltages)
//        }

        return requiredPresses
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 7)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()

    part2(testInput).println()
    check(part2(testInput) == 33)
    part2(input).println()
}

private fun Array<Int>.calculateOddities(): Array<Boolean> {
    val oddities = Array(this.size) { false }
    for (i in indices) {
        if (this[i] % 2 != 0) {
            oddities[i] = true
        }
    }
    return oddities
}

private fun Machine.matchOddity(targetOddities: Array<Boolean>): List<List<Boolean>> {
    val initialOddities = Array(targetOddities.size) { false }
    val initialButtonSelection = buttons.map { false }
    return matchOddityRec(
        curOddities = initialOddities,
        targetOddities = targetOddities,
        buttonSelection = initialButtonSelection,
        curButtonIndex = 0
    )
}

private fun Machine.matchOddityRec(
    curOddities: Array<Boolean>,
    targetOddities: Array<Boolean>,
    buttonSelection: List<Boolean>,
    curButtonIndex: Int,
): List<List<Boolean>> {
    if (curOddities.sameAs(targetOddities)) {
        return listOf(buttonSelection)
    }
    if (curButtonIndex == buttonSelection.size) {
        return listOf()
    }

    val successfulCombinations = mutableListOf<List<Boolean>>()
    listOf(true, false).forEach { pressButton ->
        val newOddities = curOddities.clone()
        val newButtonSelection = buttonSelection.toMutableList()
        if (pressButton) {
            newOddities.press(buttons[curButtonIndex])
            newButtonSelection[curButtonIndex] = true
        }
        val result = matchOddityRec(
            curOddities = newOddities,
            targetOddities = targetOddities,
            buttonSelection = newButtonSelection,
            curButtonIndex = curButtonIndex + 1
        )
        successfulCombinations.addAll(result)

    }
    return successfulCombinations
}

private fun Machine.matchJoltages(initialJoltages: Array<Int>): Int {
    knownStates.clear()
    return matchJoltagesRec(initialJoltages, 0)
}

private val knownStates = HashMap<List<Int>, Int>()
private fun Machine.matchJoltagesRec(
    curJoltages: Array<Int>,
    pressedButtonCount: Int
): Int {
    if (knownStates.containsKey(curJoltages.toList())) {
//        return knownStates.getValue(curJoltages.toList())
    }

    if (curJoltages.indices.any { index -> curJoltages[index] > targetJoltages[index] }) {
        return Int.MAX_VALUE
    }

    if (knownStates.size > 0 && pressedButtonCount >= knownStates.values.min()) {
//        return Int.MAX_VALUE
    }

    if (curJoltages.filterIndexed { index, value -> value == targetJoltages[index] }.size == curJoltages.size) {
        return pressedButtonCount
    }

    var minResult = Int.MAX_VALUE
    buttons.forEach { button ->
        val newJoltages = curJoltages.clone()
        newJoltages.pressTwice(button)
        val result = matchJoltagesRec(newJoltages, pressedButtonCount + 2)
        if (result < minResult) {
            minResult = result
        }
    }
    knownStates[curJoltages.toList()] = minResult
    return minResult
}

private fun Array<Boolean>.press(button: Button) {
    button.affectedIndices.forEach { index ->
        this[index] = !this[index]
    }
}

private fun Array<Int>.pressTwice(button: Button) {
    button.affectedIndices.forEach { index ->
        this[index] = this[index] + 2
    }
}

private fun Array<Boolean>.sameAs(other: Array<Boolean>): Boolean {
    for (i in indices) {
        if (this[i] != other[i]) {
            return false
        }
    }
    return true
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
                targetLightsState = part1.map { it == '#' }.toTypedArray(),
                buttons = buttonsList.toTypedArray(),
                targetJoltages = joltages.toTypedArray()
            )
        )
    }

    return list
}

private class Machine(
    val targetLightsState: Array<Boolean>,
    val buttons: Array<Button>,
    val targetJoltages: Array<Int>
)

private data class Button(
    val affectedIndices: List<Int>
)