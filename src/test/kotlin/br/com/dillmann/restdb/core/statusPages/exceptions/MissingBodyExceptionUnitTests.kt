package br.com.dillmann.restdb.core.statusPages.exceptions

import io.ktor.http.HttpStatusCode
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class MissingBodyExceptionUnitTests {

    private val exception = MissingBodyException()

    @Test
    fun `it should have bad request http status`() {
        // validation
        assertEquals(HttpStatusCode.BadRequest, exception.statusCode)
    }

    @Test
    fun `it should have expected error description`() {
        // scenario
        val expectedMessage = "Request body is required and cannot be empty"

        // validation
        assertEquals(expectedMessage, exception.message)
    }

}