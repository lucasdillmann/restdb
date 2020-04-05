package br.com.dillmann.restdb.core.filterDsl.utils

import org.junit.Test
import kotlin.test.assertTrue

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
class ParameterIdGeneratorUnitTests {

    @Test
    fun `it should return a random value in expected format`() {
        // scenario
        val expectedFormat = "^param_[a-z0-9]{12}$".toRegex()

        // execution
        val result = randomParameterId()

        // validation
        assertTrue { expectedFormat.matches(result) }
    }

}