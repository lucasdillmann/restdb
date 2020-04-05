package br.com.dillmann.restdb.core.filterDsl.jdbc.exception

import br.com.dillmann.restdb.testUtils.randomPositiveInt
import br.com.dillmann.restdb.testUtils.randomString
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
class InvalidParameterCountExceptionUnitTests {

    private val filterName = randomString()
    private val columnName = randomString()
    private val discriminator = randomString()
    private val currentCount = randomPositiveInt()
    private val expectedCount = randomPositiveInt()
    private val exception =
        InvalidParameterCountException(filterName, columnName, currentCount, expectedCount, discriminator)

    @Test
    fun `it should have an human readable description`() {
        // scenario
        val expectedMessage = "Invalid parameters on filter $filterName of column $columnName. " +
                "Token has $currentCount value(s) but it expects $discriminator $expectedCount value(s)."

        // execution
        val currentMessage = exception.message

        // validation
        assertEquals(expectedMessage, currentMessage)
    }
}