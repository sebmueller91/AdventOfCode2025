private const val DAY = 10

fun main() {
    fun part1(input: List<String>): Int {
        var requiredPresses = 0
        input.parse().forEach { machine ->
            val successfulCombinations = machine.matchOddities(machine.targetLightsState)
            requiredPresses += successfulCombinations.minOf { it.count { it } }
        }
        return requiredPresses
    }

    fun part2(input: List<String>): Int {
        var requiredPresses = 0
        input.parse().forEachIndexed {index, machine ->
            requiredPresses += machine.matchJoltages()
        }

        return requiredPresses
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 7)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()

    check(part2(testInput) == 33)
    part2(input).println()
}

private fun Machine.matchJoltages(): Int {
    knownStates.clear()
    return matchJoltagesRec(targetJoltages = targetJoltages)
}


private val knownStates = HashMap<List<Int>, Int>()
private fun Machine.matchJoltagesRec(targetJoltages: Array<Int>): Int {
    if (knownStates.containsKey(targetJoltages.toList())) {
        return knownStates.getValue(targetJoltages.toList())
    }
    if (targetJoltages.joltageOverflow()) {
        return Int.MAX_VALUE
    }

    if (targetJoltages.reachedTargetJoltage()) {
        return 0
    }

    val targetOddities = targetJoltages.calculateOddities()
    val successfulCombinations = matchOddities(targetOddities)

    var minResult = Int.MAX_VALUE
    successfulCombinations.forEach { combination ->
        val newJoltagesState = Array(targetJoltages.size) {0}.applyButtonCombination(combination, buttons)
        val newTargetJoltages = targetJoltages.minus(newJoltagesState).divideBy(2)

        val preResult = matchJoltagesRec(newTargetJoltages)
        val result = combination.count { it } + 2 * preResult
        if (preResult != Int.MAX_VALUE && result < minResult) {
            minResult = result
        }
    }

    knownStates[targetJoltages.toList()] = minResult
    return minResult
}

private fun<T> Array<T>.print(identifier: String) {
    "$identifier: ${this.toList()}".println()
}

private fun Array<Int>.applyButtonCombination(combination: List<Boolean>, buttons: Array<Button>): Array<Int> {
    val newArray = this.clone()
    combination.forEachIndexed { index, pressButton ->
        if (pressButton) {
            newArray.press(buttons[index])
        }
    }
    return newArray
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

private fun Array<Int>.minus(other: Array<Int>): Array<Int> {
    val newArray = Array (this.size) {0}
    forEachIndexed { index, value ->
        newArray[index] = value - other[index]
    }
    return newArray
}

private fun Array<Int>.divideBy(factor: Int): Array<Int> {
    val newArray = Array (this.size) {0}
    forEachIndexed { index, value ->
        newArray[index] = value / factor
    }
    return newArray
}

private fun Array<Int>.joltageOverflow(): Boolean {
    forEachIndexed { index, value ->
        if (value < 0) {
            return true
        }
    }
    return false
}

private fun Array<Int>.reachedTargetJoltage(): Boolean {
    forEachIndexed { index, value ->
        if (value != 0) {
            return false
        }
    }
    return true
}


private fun Machine.matchOddities(targetOddities: Array<Boolean>): List<List<Boolean>> {
    val initialOddities = Array(targetOddities.size) { false }
    val initialButtonSelection = buttons.map { false }
    return matchOdditiesRec(
        curOddities = initialOddities,
        targetOddities = targetOddities,
        buttonSelection = initialButtonSelection,
        curButtonIndex = 0
    )
}

private fun Machine.matchOdditiesRec(
    curOddities: Array<Boolean>,
    targetOddities: Array<Boolean>,
    buttonSelection: List<Boolean>,
    curButtonIndex: Int,
): List<List<Boolean>> {
    if (curButtonIndex == buttonSelection.size) {
        return if (curOddities.sameAs(targetOddities)) {
            listOf(buttonSelection)
        } else {
            listOf()
        }
    }

    val successfulCombinations = mutableListOf<List<Boolean>>()
    listOf(true, false).forEach { pressButton ->
        val newOddities = curOddities.clone()
        val newButtonSelection = buttonSelection.toMutableList()
        if (pressButton) {
            newOddities.press(buttons[curButtonIndex])
            newButtonSelection[curButtonIndex] = true
        }
        val result = matchOdditiesRec(
            curOddities = newOddities,
            targetOddities = targetOddities,
            buttonSelection = newButtonSelection,
            curButtonIndex = curButtonIndex + 1
        )
        successfulCombinations.addAll(result)

    }
    return successfulCombinations
}

private fun Array<Boolean>.press(button: Button) {
    button.affectedIndices.forEach { index ->
        this[index] = !this[index]
    }
}

private fun Array<Int>.press(button: Button) {
    button.affectedIndices.forEach { index ->
        this[index] = this[index] + 1
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