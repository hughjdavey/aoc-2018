package days

import util.infiniteList
import util.scan
import util.toInt

class Day16 : Day(16) {

    private val untilStartOfTestProgram = inputList.lastIndexOf(inputList.last { it.startsWith("After") }) + 2
    private val samples = parseSamples(inputList.take(untilStartOfTestProgram))
    private val program = parseTestProgram(inputList.drop(untilStartOfTestProgram + 2))

    override fun partOne(): Any {
        return samples.fold(0) { acc, sample ->
            val possibilities = Day16.getPossibilities(sample)
            if (possibilities.size >= 3) acc + 1 else acc
        }
    }

    override fun partTwo(): Any {
        val opcodeValues = findOpcodeValues(samples)

        val registers = intArrayOf(0, 0, 0, 0)
        program.forEach {
            Op.invoke(opcodeValues[it[0]]!!, registers, it[1], it[2], it[3])
        }
        return registers[0]
    }

    enum class Op(val fx: (Int, Int) -> Int) {
        add({a, b -> a + b}),
        mul({a, b -> a * b}),
        ban({a, b -> a.and(b)}),
        bor({a, b -> a.or(b)}),
        set({a, _ -> a}),
        gt({a, b -> (a > b).toInt()}),
        eq({a, b -> (a == b).toInt()});

        companion object {

            // todo logic here could be deduped
            fun invoke(name: String, registers: IntArray, a: Int, b: Int, c: Int) {
                if (name.startsWith("set")) {
                    val interpretation = name.last()
                    val op = Op.valueOf(name.dropLast(1))

                    when (interpretation) {
                        'r' -> registers[c] = op.fx(registers[a], 0)
                        'i' -> registers[c] = op.fx(a, 0)
                    }
                }
                else if (!name.startsWith('g') && !name.startsWith('e')) {
                    val interpretation = name.last()
                    val op = Op.valueOf(name.dropLast(1))

                    when (interpretation) {
                        'r' -> registers[c] = op.fx(registers[a], registers[b])
                        'i' -> registers[c] = op.fx(registers[a], b)
                    }
                }
                else {
                    val interpretation = name.takeLast(2)
                    val op = Op.valueOf(name.dropLast(2))

                    when (interpretation) {
                        "ir" -> registers[c] = op.fx(a, registers[b])
                        "ri" -> registers[c] = op.fx(registers[a], b)
                        "rr" -> registers[c] = op.fx(registers[a], registers[b])
                    }
                }
            }
        }
    }

    // todo could remove this annoying equals impl by swapping array for list?
    data class Sample(val before: IntArray, val after: IntArray, val op: Int, val a: Int, val b: Int, val c: Int) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Sample

            if (!before.contentEquals(other.before)) return false
            if (!after.contentEquals(other.after)) return false
            if (op != other.op) return false
            if (a != other.a) return false
            if (b != other.b) return false
            if (c != other.c) return false

            return true
        }

        override fun hashCode(): Int {
            var result = before.contentHashCode()
            result = 31 * result + after.contentHashCode()
            result = 31 * result + op
            result = 31 * result + a
            result = 31 * result + b
            result = 31 * result + c
            return result
        }
    }

    companion object {

        private val ALL_INSTRUCTIONS = setOf("addi", "addr", "muli", "mulr", "bani", "banr", "borr", "bori", "seti", "setr", "gtir", "gtri", "gtrr", "eqir", "eqri", "eqrr")

        fun findOpcodeValues(samples: List<Sample>): Map<Int, String> {
            return infiniteList(samples).scan(mapOf<Int, String>()) { map, sample ->
                val possibilities = getPossibilities(sample).filter { it !in map.values }
                if (possibilities.size == 1) {
                    map.plus(sample.op to possibilities.first())
                } else map
            }.find { it.keys.size == ALL_INSTRUCTIONS.size }!!
        }

        fun getPossibilities(sample: Sample): List<String> {
            return ALL_INSTRUCTIONS.map { ins ->
                val registers = sample.before.clone()
                Op.invoke(ins, registers, sample.a, sample.b, sample.c)
                if (registers.contentEquals(sample.after)) ins else null
            }.filterNotNull()
        }

        fun parseSamples(input: List<String>): List<Sample> {
            return input.windowed(4, 4).map { sample ->
                val before = sample[0].substringAfter("[").dropLast(1).split(", ").map { it.toInt() }.toIntArray()
                val after = sample[2].substringAfter("[").dropLast(1).split(", ").map { it.toInt() }.toIntArray()
                val instruction = parseInstruction(sample[1])
                Sample(before, after, instruction[0], instruction[1], instruction[2], instruction[3])
            }
        }

        fun parseTestProgram(input: List<String>): List<List<Int>> {
            return input.map { parseInstruction(it) }
        }

        private fun parseInstruction(input: String): List<Int> {
            return input.split(" ").map { it.toInt() }
        }
    }
}
