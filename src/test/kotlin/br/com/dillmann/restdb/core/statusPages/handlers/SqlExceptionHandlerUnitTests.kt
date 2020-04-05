package br.com.dillmann.restdb.core.statusPages.handlers

import br.com.dillmann.restdb.core.statusPages.logger
import br.com.dillmann.restdb.core.statusPages.utils.randomSqlException
import br.com.dillmann.restdb.testUtils.randomString
import io.ktor.http.HttpStatusCode
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.sql.SQLException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class SqlExceptionHandlerUnitTests {

    private val handler = captureHandler<SQLException> { sqlExceptionHandler() }

    @Before
    fun setUp() {
        mockkObject(logger)
        every { logger.error(any(), any<Exception>()) } just Runs
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `it should have expected status code`() {
        // scenario
        val exception = randomSqlException()

        // execution
        val (status, _) = handler(exception)

        // validation
        assertEquals(HttpStatusCode.UnprocessableEntity, status)
    }

    @Test
    fun `it should have expected message`() {
        // scenario
        val exception = randomSqlException()

        // execution
        val (_, body) = handler(exception)

        // validation
        require(body is Map<*, *>)
        assertEquals("Action rejected by database", body["message"])
    }

    @Test
    fun `it should have exception details set`() {
        // scenario
        val exception = randomSqlException()

        // execution
        val (_, body) = handler(exception)

        // validation
        require(body is Map<*, *>)
        assertNotNull(body["details"])
    }

    @Test
    fun `it should log exception details`() {
        // scenario
        val expectedMessage = randomString()
        val exception = randomSqlException()
        every { exception.message } returns expectedMessage

        // execution
        handler(exception)

        // validation
        verify { logger.error(expectedMessage, exception) }
    }
}