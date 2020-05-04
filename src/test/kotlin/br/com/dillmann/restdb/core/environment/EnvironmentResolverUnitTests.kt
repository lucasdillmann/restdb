package br.com.dillmann.restdb.core.environment

import org.junit.Test
import kotlin.test.assertTrue

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-05-02
 */
class EnvironmentResolverUnitTests {

    @Test
    fun `it should return all environment variables`() {
        // scenario
        val expectedResult: Map<String, String> = System.getenv()

        // execution
        val result = EnvironmentResolver.values()

        // validation
        assertTrue { containsSameValues(expectedResult, result) }
    }

    private fun containsSameValues(left: Map<String, String>, right: Map<String, String>): Boolean {
        return left.size == right.size
                && left.all { (key, value) -> right[key] == value }
                && right.all { (key, value) -> left[key] == value }
    }

}