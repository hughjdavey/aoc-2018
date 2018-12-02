package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection.empty
import org.hamcrest.collection.IsIterableContainingInOrder.contains
import org.hamcrest.core.Is.`is`
import org.junit.Test
import util.lazyAllPossiblePairs

/**
 * Tests for the days package
 */
class DaysTest {

    @Test
    fun testDayOneFreqSeenTracker() {
        assertThat(Day1.FreqSeenTracker.seenFreqs, empty())

        val a = Day1.FreqSeenTracker()
        assertThat(a.currentFreq, `is`(0))
        assertThat(a.seenBefore, `is`(false))
        assertThat(Day1.FreqSeenTracker.seenFreqs, empty())

        val b = Day1.FreqSeenTracker(1)
        assertThat(b.currentFreq, `is`(1))
        assertThat(b.seenBefore, `is`(false))
        assertThat(Day1.FreqSeenTracker.seenFreqs, contains(1))

        val c = Day1.FreqSeenTracker(1)
        assertThat(c.currentFreq, `is`(1))
        assertThat(c.seenBefore, `is`(true))
        val d = Day1.FreqSeenTracker(2)
        assertThat(d.currentFreq, `is`(2))
        assertThat(d.seenBefore, `is`(false))
        val e = Day1.FreqSeenTracker(3)
        assertThat(e.currentFreq, `is`(3))
        assertThat(e.seenBefore, `is`(false))
        assertThat(Day1.FreqSeenTracker.seenFreqs, contains(1, 2, 3))
    }

    @Test
    fun testDayTwoPartOneToMatchSummary() {
        // use the example box ids from day 2 part 1 description
        val boxIds = listOf("abcdef", "bababc", "abbcde", "abcccd", "aabcdd", "abcdee", "ababab")
        assertThat(boxIds.map { Day2.toMatchSummary(it) }, contains(
            Pair(false, false),
            Pair(true, true),
            Pair(true, false),
            Pair(false, true),
            Pair(true, false),
            Pair(true, false),
            Pair(false, true)
        ))
    }

    @Test
    fun testDayTwoPartTwoCommonLetters() {
        // use the example box ids from day 2 part 2 description
        assertThat(Day2.commonLetters("fghij", "fguij"), `is`("fgij"))

        val boxIds = listOf("abc", "def", "adc")
        val commonLettersForAllPairs = lazyAllPossiblePairs(boxIds, boxIds).map { Day2.commonLetters(it.first, it.second) }.take(9).toList()
        assertThat(commonLettersForAllPairs, contains(
            "abc", "", "ac", "", "def", "", "ac", "", "adc"
        ))
    }
}
