package br.com.dillmann.restdb.core.statusPages.handlers

import br.com.dillmann.restdb.testUtils.randomString
import com.google.gson.JsonParseException
import io.ktor.http.HttpStatusCode
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class JsonParseExceptionHandlerUnitTests {

    private val handler = captureHandler<JsonParseException> { jsonParserExceptionHandler() }

    @Test
    fun `it should have expected status code`() {
        // scenario
        val exception = JsonParseException(randomString())

        // execution
        val (status, _) = handler(exception)

        // validation
        assertEquals(HttpStatusCode.BadRequest, status)
    }

    @Test
    fun `it should have expected message`() {
        // scenario
        val exception = JsonParseException(randomString())

        // execution
        val (_, body) = handler(exception)

        // validation
        require(body is Map<*, *>)
        assertEquals("Request body is not a valid JSON", body["message"])
    }
}