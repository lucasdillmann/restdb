package br.com.dillmann.restdb.core.statusPages.exceptions

import io.ktor.http.HttpStatusCode
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class ValidationExceptionUnitTests {

    private val exception = ValidationException(randomErrorDetailsList())

    @Test
    fun `it should have unprocessable entity http status`() {
        // validation
        assertEquals(HttpStatusCode.UnprocessableEntity, exception.statusCode)
    }

    @Test
    fun `it should have expected error description`() {
        // scenario
        val expectedMessage = "Your request cannot be accepted. Check the problems found and try again."

        // validation
        assertEquals(expectedMessage, exception.message)
    }

}