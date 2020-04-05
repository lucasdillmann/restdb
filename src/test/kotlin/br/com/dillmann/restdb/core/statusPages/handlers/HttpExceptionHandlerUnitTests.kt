package br.com.dillmann.restdb.core.statusPages.handlers

import br.com.dillmann.restdb.core.statusPages.exceptions.HttpException
import br.com.dillmann.restdb.testUtils.randomString
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class HttpExceptionHandlerUnitTests {

    private val handler = captureHandler<HttpException> { httpExceptionHandler() }

    @Test
    fun `it should have expected status code`() {
        // scenario
        val expectedStatus = randomHttpStatusCode()
        val exception = object : HttpException(randomString(), expectedStatus) {}

        // execution
        val (status, _) = handler(exception)

        // validation
        assertEquals(expectedStatus, status)
    }

    @Test
    fun `it should have expected message`() {
        // scenario
        val expectedMessage = randomString()
        val exception = object : HttpException(expectedMessage, randomHttpStatusCode()) {}

        // execution
        val (_, body) = handler(exception)

        // validation
        require(body is Map<*, *>)
        assertEquals(expectedMessage, body["message"])
    }
}