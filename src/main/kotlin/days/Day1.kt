package days

import util.infiniteList
import util.scan

class Day1 : Day(1) {

    override fun partOne(): Int {
        return inputList.fold(0) { sum, freq ->
            sum + freq.toInt()
        }
    }

    override fun partTwo(): Int {
        val bar = infiniteList(inputList).scan(FreqSeenTracker()) { foo, freq ->
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
