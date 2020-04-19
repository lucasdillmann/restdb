package br.com.dillmann.restdb.core.statusPages.handlers

import br.com.dillmann.restdb.core.statusPages.logger
import br.com.dillmann.restdb.testUtils.randomInt
import br.com.dillmann.restdb.testUtils.randomLong
import br.com.dillmann.restdb.testUtils.randomString
import com.fasterxml.jackson.core.JsonLocation
import com.fasterxml.jackson.core.JsonProcessingException
import io.ktor.http.HttpStatusCode
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class JsonParseExceptionHandlerUnitTests {

    private val handler = captureHandler<JsonProcessingException> { jsonProcessingExceptionHandler() }
    private val exception: JsonProcessingException = mockk()
    private val location: JsonLocation = mockk()

    @Before
    fun setUp() {
        mockkObject(logger)
        every { logger.error(any<String>(), any()) } just Runs
        every { exception.location } returns location
        every { exception.message } returns randomString()
        every { location.lineNr } returns randomInt()
        every { location.columnNr } returns randomInt()
        every { location.charOffset } returns randomLong()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `it should have expected status code`() {
        // execution
        val (status, _) = handler(exception)

        // validation
        assertEquals(HttpStatusCode.BadRequest, status)
    }

    @Test
    fun `it should have expected message`() {
        // execution
        val (_, body) = handler(exception)

        // validation
        require(body is Map<*, *>)
        assertEquals("Request body is not a valid JSON", body["message"])
    }

    @Test
    fun `it should have column number in response details`() {
        // scenario
        val expectedValue = randomInt()
        every { location.columnNr } returns expectedValue

        // execution
        val (_, body) = handler(exception)

        // validation
        require(body is Map<*, *>)
        val details = body["details"]
        require(details is Map<*, *>)
        assertEquals(expectedValue, details["column"])
    }

    @Test
    fun `it should have line number in response details`() {
        // scenario
        val expectedValue = randomInt()
        every { location.lineNr } returns expectedValue

        // execution
        val (_, body) = handler(exception)

        // validation
        require(body is Map<*, *>)
        val details = body["details"]
        require(details is Map<*, *>)
        assertEquals(expectedValue, details["line"])
    }

    @Test
    fun `it should have char offset in response details`() {
        // scenario
        val expectedValue = randomLong()
        every { location.charOffset } returns expectedValue

        // execution
        val (_, body) = handler(exception)

        // validation
        require(body is Map<*, *>)
        val details = body["details"]
        require(details is Map<*, *>)
        assertEquals(expectedValue, details["charOffset"])
    }

    @Test
    fun `it should have error message in response details`() {
        // scenario
        val expectedValue = randomString()
        every { exception.message } returns expectedValue

        // execution
        val (_, body) = handler(exception)

        // validation
        require(body is Map<*, *>)
        val details = body["details"]
        require(details is Map<*, *>)
        assertEquals(expectedValue, details["message"])
    }
}