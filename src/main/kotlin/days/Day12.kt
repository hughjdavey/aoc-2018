package days

import com.google.common.collect.EvictingQueue

class Day12 : Day(12) {

    private val initialState = parseInitialState(inputList.first())
    private val rules = parseRules(inputList.drop(2))

    override fun partOne(): Any {
        val twentyGenerations = doGenerations(initialState, rules, 20)
        return totalPlantContainingPots(twentyGenerations)
    }

    override fun partTwo(): Any {
        val fiftyBillionGenerations = doGenerations(initialState, rules, 50000000000)
        return totalPlantContainingPots(fiftyBillionGenerations)
    }

    data class Rule(val rule: String, val result: Char)

    companion object {

        fun parseInitialState(input: String) = input.substring(15)

        fun parseRules(input: List<String>) = input.map { it.trim() }.map { Rule(it.take(5), it.last()) }

        fun totalPlantContainingPots(state: Pair<String, Int>): Long {
            // todo could clean this up a bit!
            val (remainingIterations, repeatingSum) = if (state.first.contains("::")) {
                50000000000 - state.first.substring(state.first.indexOf("::") + 2, state.first.lastIndexOf("::")).toLong() to
                        state.first.substring(state.first.lastIndexOf("::") + 2).toLong()
            } else 0L to 0L

            return state.first.foldIndexed(0) { index, total, pot ->
                if (pot == '#') total + (index - state.second) else total
            } + (remainingIterations * repeatingSum)

            //9699999999321 correct!
        }

        fun doGenerations(initialState: String, rules: List<Rule>, generations: Long): Pair<String, Int> {
            var state = initialState to 0
            var lastSum = 0L

            val last3Diffs = EvictingQueue.create<Long>(3)

            (0 until generations).forEach {
                val sum = totalPlantContainingPots(state)
                val diff = sum - lastSum
                lastSum = sum
                last3Diffs.add(diff)

                // if diffs have started repeating, return early and add info needed to extrapolate
                if (last3Diffs.size == 3 && last3Diffs.all { it == diff }) {
                    return "${state.first}::$it::$diff" to state.second
                }
                state = doGeneration(state, rules)
            }
            return state
        }

        fun doGeneration(initialState: Pair<String, Int>, rules: List<Rule>): Pair<String, Int> {
            val padding = padState(initialState.first)
            var state = padding.first.replace('#', '.')

            padding.first.windowed(5, 1).forEachIndexed { index, window ->
                rules.forEach { rule ->
                    if (rule.rule == window) {
                        state = state.replaceRange(index + 2, index + 3, rule.result.toString())
                    }
                }
            }

            return state to initialState.second + padding.second
        }

        fun padState(state: String): Pair<String, Int> {

            val start = when {
                state.startsWith(".....") -> 0
                state.startsWith("....") -> 1
                state.startsWith("...") -> 2
                state.startsWith("..") -> 3
                state.startsWith(".") -> 4
                else -> 5
            }

            val end = when {
                state.endsWith(".....") -> 0
                state.endsWith("....") -> 1
                state.endsWith("...") -> 2
                state.endsWith("..") -> 3
                state.endsWith(".") -> 4
                else -> 5
            }

            return "${".".repeat(start)}$state${".".repeat(end)}" to start
        }
    }
}
