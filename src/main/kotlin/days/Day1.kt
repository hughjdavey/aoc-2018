package days

import util.InputReader
import util.infiniteList
import util.scan

class Day1 : Day {

    override fun partOne(): Int {
        val frequencies = InputReader.getInputAsList(1)
        return frequencies.fold(0) { sum, freq ->
            sum + freq.toInt()
        }
    }

    override fun partTwo(): Int {
        val frequencies = InputReader.getInputAsList(1)
        val bar = infiniteList(frequencies).scan(FreqSeenTracker()) { foo, freq ->
            FreqSeenTracker(foo.currentFreq + freq.toInt())
        }
        return bar.find { it.seenBefore }!!.currentFreq
    }

    class FreqSeenTracker(freq: Int? = null) {
        val currentFreq = freq ?: 0
        val seenBefore = if (freq != null) !FreqSeenTracker.seenFreqs.add(freq) else false

        companion object {
            val seenFreqs: MutableSet<Int> = mutableSetOf()
        }
    }
}
