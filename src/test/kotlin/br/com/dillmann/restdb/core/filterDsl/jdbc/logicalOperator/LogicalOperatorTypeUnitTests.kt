package br.com.dillmann.restdb.core.filterDsl.jdbc.logicalOperator

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
@RunWith(Parameterized::class)
class LogicalOperatorTypeUnitTests(private val scenario: Scenario) {

    /**
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-04-02
     */
    data class Scenario(
        val type: LogicalOperatorType,
        val identifier: String
    )

    companion object {
        @JvmStatic
        @get:Parameterized.Parameters(name = "{0}")
        val data = listOf(
            Scenario(LogicalOperatorType.AND, "&&"),
            Scenario(LogicalOperatorType.OR, "||")
        )
    }

    @Test
    fun `it should be able to be located using identifier`() {
        // scenario
        val (type, identifier) = scenario

        // execution
        val result = LogicalOperatorType.fromIdentifier(identifier)

        // validation
        assertEquals(type, result)
    }
}