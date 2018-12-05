package util

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIterableContainingInOrder.contains
import org.hamcrest.core.Is.`is`
import org.junit.Test

/**
 * Tests for the utilities package
 */
class UtilTest {

    private val oneToFive = listOf(1, 2, 3, 4, 5)

    @Test
    fun testReadInputAsString() {
        val testInputAsString = InputReader.getInputAsString(1)
        assertThat(testInputAsString, `is`("this\nis\na\ntest input\nfile\n"))
    }

    @Test
    fun testReadInputAsList() {
        val testInputAsList = InputReader.getInputAsList(1)
        assertThat(testInputAsList, contains("this", "is", "a", "test input", "file"))
    }

    @Test
    fun testInfiniteList() {
        val infiniteList = infiniteList(oneToFive)
        assertThat(infiniteList.take(11).toList(), contains(1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1))
    }

    @Test
    fun testScanLeft() {
        assertThat(infiniteList(oneToFive).scan(0) { acc, elem -> acc + elem }.take(5).toList(), contains(0, 1, 3, 6, 10))
        assertThat(infiniteList(oneToFive).scan(0) { acc, elem -> acc + elem }.take(10).drop(5).toList(), contains(15, 16, 18, 21, 25))
    }

    @Test
    fun testLazyAllPossiblePairs() {
        val allPossiblePairs = lazyAllPossiblePairs(listOf(1, 2, 3), listOf("a", "b", "c"))
        assertThat(allPossiblePairs.map { it.first.toString() + it.second }.take(5).toList(), contains("1a", "1b", "1c", "2a", "2b"))
        assertThat(allPossiblePairs.map { it.first.toString() + it.second }.take(9).toList(), contains("1a", "1b", "1c", "2a", "2b", "2c", "3a", "3b", "3c"))
        assertThat(allPossiblePairs.map { it.first.toString() + it.second }.take(20).toList(), contains("1a", "1b", "1c", "2a", "2b", "2c", "3a", "3b", "3c"))
    }

    @Test
    fun testBooleanToInt() {
        assertThat(true.toInt(), `is`(1))
        assertThat(false.toInt(), `is`(0))
    }

    @Test
    fun testCharSameLetterDifferentCase() {
        assertThat('h'.sameLetterDifferentCase('h'), `is`(false))
        assertThat('H'.sameLetterDifferentCase('H'), `is`(false))
        assertThat('h'.sameLetterDifferentCase('H'), `is`(true))
        assertThat('H'.sameLetterDifferentCase('h'), `is`(true))
    }
}
