package br.com.dillmann.restdb.core.statusPages.handlers

import br.com.dillmann.restdb.core.filterDsl.FilterDslSyntaxException
import io.ktor.http.HttpStatusCode
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class FilterDslSyntaxExceptionHandlerUnitTests {

    private val handler = captureHandler<FilterDslSyntaxException> { filterDslSyntaxExceptionHandler() }

    @Test
    fun `response should have bad request status code`() {
        // scenario
        val exception = randomFilterDslSyntaxException()

        // execution
        val (status, _) = handler(exception)

        // validation
        assertEquals(HttpStatusCode.BadRequest, status)
    }

    @Test
    fun `response body should have expected message`() {
        // scenario
        val exception = randomFilterDslSyntaxException()

        // execution
        val (_, body) = handler(exception)

        // validation
        require(body is Map<*, *>)
        assertEquals(exception.message, body["message"])
    }

    @Test
    fun `response body should have expected line in details`() {
        // scenario
        val exception = randomFilterDslSyntaxException()

        // execution
        val (_, body) = handler(exception)

        // validation
        require(body is Map<*, *>)
        val details = body["details"]
        require(details is Map<*, *>)
        assertEquals(exception.line, details["line"])
    }

    @Test
    fun `response body should have expected description in details`() {
        // scenario
        val exception = randomFilterDslSyntaxException()

        // execution
        val (_, body) = handler(exception)

        // validation
        require(body is Map<*, *>)
        val details = body["details"]
        require(details is Map<*, *>)
        assertEquals(exception.description, details["description"])
    }
}