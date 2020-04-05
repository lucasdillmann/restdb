package br.com.dillmann.restdb.core.filterDsl

import br.com.dillmann.restdb.testUtils.expect
import br.com.dillmann.restdb.testUtils.randomPositiveInt
import br.com.dillmann.restdb.testUtils.randomString
import br.com.dillmann.restdb.testUtils.randomUuid
import io.mockk.mockk
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class FilterDslErrorListenerUnitTests {

    @get:Rule
    val expectedException: ExpectedException = ExpectedException.none()

    @Test
    fun `it should throw an error when a syntax error is detected`() {
        // scenario
        val lineNumber = randomPositiveInt()
        val position = randomPositiveInt()
        val symbol = randomUuid()
        val description = randomString()

        // validation
        expectedException.expect<FilterDslSyntaxException>("Filter syntax is invalid") {
            allOf(
                hasProperty("line", equalTo(lineNumber)),
                hasProperty("position", equalTo(position)),
                hasProperty("symbol", equalTo(symbol)),
                hasProperty("description", equalTo(description))
            )
        }

        // execution
        FilterDslErrorListener.syntaxError(mockk(), symbol, lineNumber, position, description, mockk())
    }

}