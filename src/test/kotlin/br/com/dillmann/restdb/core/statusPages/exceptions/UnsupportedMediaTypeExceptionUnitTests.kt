package br.com.dillmann.restdb.core.statusPages.exceptions

import io.ktor.http.HttpStatusCode
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class UnsupportedMediaTypeExceptionUnitTests {

    private val exception = UnsupportedMediaTypeException()

    @Test
    fun `it should have unsupported media type http status`() {
        // validation
        assertEquals(HttpStatusCode.UnsupportedMediaType, exception.statusCode)
    }

    @Test
    fun `it should have expected error description`() {
        // scenario
        val expectedMessage = "Request media type is not supported. Retry with a JSON body."

        // validation
        assertEquals(expectedMessage, exception.message)
    }

}