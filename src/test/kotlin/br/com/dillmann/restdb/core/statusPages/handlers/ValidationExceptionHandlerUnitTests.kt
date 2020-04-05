package br.com.dillmann.restdb.core.statusPages.handlers

import br.com.dillmann.restdb.core.statusPages.exceptions.ValidationException
import br.com.dillmann.restdb.core.statusPages.exceptions.randomErrorDetailsList
import io.ktor.http.HttpStatusCode
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class ValidationExceptionHandlerUnitTests {

    private val handler = captureHandler<ValidationException> { validationExceptionHandler() }

    @Test
    fun `it should have expected status code`() {
        // scenario
        val exception = randomValidationException()

        // execution
        val (status, _) = handler(exception)

        // validation
        assertEquals(HttpStatusCode.UnprocessableEntity, status)
    }

    @Test
    fun `it should have expected message`() {
        // scenario
        val exception = randomValidationException()

        // execution
        val (_, body) = handler(exception)

        // validation
        require(body is Map<*, *>)
        assertEquals("Your request cannot be accepted. Check the problems found and try again.", body["message"])
    }

    @Test
    fun `it should have exception details set`() {
        // scenario
        val expectedDetails = randomErrorDetailsList()
        val exception = ValidationException(expectedDetails)

        // execution
        val (_, body) = handler(exception)

        // validation
        require(body is Map<*, *>)
        assertEquals(expectedDetails, body["details"])
    }

}