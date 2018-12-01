package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIterableContainingInOrder.contains
import org.hamcrest.core.Is.`is`
import org.junit.Test
import util.InputReader

/**
 * Class to test the various helper constructs found in and around day classes
 */
class DayHelperTest {

    @Test
    fun testReadInputAsString() {
        val testInputAsString = InputReader.getInputAsString(0)
        assertThat(testInputAsString, `is`("this\nis\na\ntest input\nfile\n"))
    }

    @Test
    fun testReadInputAsList() {
        val testInputAsList = InputReader.getInputAsList(0)
        assertThat(testInputAsList, contains("this", "is", "a", "test input", "file"))
    }

    @Test
    fun testInfiniteList() {
        val infiniteList = Day1.InfiniteList(listOf(1, 2, 3, 4, 5))
        assertThat(infiniteList.next(), `is`(1))
        assertThat(infiniteList.next(), `is`(2))
        assertThat(infiniteList.next(), `is`(3))
        assertThat(infiniteList.next(), `is`(4))
        assertThat(infiniteList.next(), `is`(5))
        assertThat(infiniteList.next(), `is`(1))
        assertThat(infiniteList.next(), `is`(2))
        assertThat(infiniteList.next(), `is`(3))
        assertThat(infiniteList.next(), `is`(4))
        assertThat(infiniteList.next(), `is`(5))
        assertThat(infiniteList.next(), `is`(1))
    }
}
