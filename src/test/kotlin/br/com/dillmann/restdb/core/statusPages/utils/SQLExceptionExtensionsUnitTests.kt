package br.com.dillmann.restdb.core.statusPages.utils

import br.com.dillmann.restdb.testUtils.randomString
import io.mockk.every
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class SQLExceptionExtensionsUnitTests {

    @Test
    fun `it should be able to convert exception details to ErrorDetails`() {
        // scenario
        val exception = randomSqlException()
        val expectedSqlState = randomString()
        val expectedMessage = randomString()
        every { exception.sqlState } returns expectedSqlState
        every { exception.message } returns expectedMessage

        // execution
        val result = exception.details()

        // validation
        assertEquals(1, result.size)
        assertEquals(expectedMessage, result.first().message)
        assertEquals(expectedSqlState, result.first().sqlState)
    }

    @Test
    fun `it should be able to recursively convert exception causes to ErrorDetails`() {
        // scenario
        val depth = (10..20).random()
        val rootException = randomSqlException()
        var pointer = rootException
        repeat(depth) {
            val childException = randomSqlException()
            every { pointer.nextException } returns childException
            pointer = childException
        }

        // execution
        val result = rootException.details()

        // validation
        assertEquals(depth + 1, result.size)

    }
}