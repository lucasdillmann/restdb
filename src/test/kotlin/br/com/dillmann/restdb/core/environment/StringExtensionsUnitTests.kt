package br.com.dillmann.restdb.core.environment

import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-26
 */
class StringExtensionsUnitTests {

    @Test
    fun `it should convert camel case to snake case`() {
        // scenario
        val input = "loremIpsumDolorSitAmet"
        val expectedOutput = "lorem_ipsum_dolor_sit_amet"

        // execution
        val result = input.camelCaseToSnakeCase()

        // validation
        assertEquals(expectedOutput, result)
    }

}