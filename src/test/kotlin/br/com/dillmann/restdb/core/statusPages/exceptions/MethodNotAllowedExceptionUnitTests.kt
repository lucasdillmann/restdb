package br.com.dillmann.restdb.core.statusPages.exceptions

import br.com.dillmann.restdb.testUtils.randomString
import io.ktor.http.HttpStatusCode
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class MethodNotAllowedExceptionUnitTests {

    private val methodName = randomString()
    private val exception = MethodNotAllowedException(methodName)

    @Test
    fun `it should have method not allowed http status`() {
        // validation
        assertEquals(HttpStatusCode.MethodNotAllowed, exception.statusCode)
    }

    @Test
    fun `it should have expected error description`() {
        // scenario
        val expectedMessage = "Request method $methodName is not allowed"

        // validation
        assertEquals(expectedMessage, exception.message)
    }

}