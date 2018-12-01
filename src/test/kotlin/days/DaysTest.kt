package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection.empty
import org.hamcrest.collection.IsIterableContainingInOrder.contains
import org.hamcrest.core.Is.`is`
import org.junit.Test

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
}
