package br.com.dillmann.restdb.core.filterDsl.jdbc.filter

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
@RunWith(Parameterized::class)
class FilterTypeUnitTests(private val scenario: Scenario) {

    /**
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-04-02
     */
    data class Scenario(
        val type: FilterType,
        val identifier: String,
        val expectedMinimum: Int?,
        val expectedMaximum: Int
    )

    companion object {
        @JvmStatic
        @get:Parameterized.Parameters(name = "{0}")
        val data = listOf(
            Scenario(FilterType.EQUALS, "equals", 1, 1),
            Scenario(FilterType.NOT_EQUALS, "notEquals", 1, 1),
            Scenario(FilterType.LESS_THAN, "lessThan", 1, 1),
            Scenario(FilterType.LESS_OR_EQUALS, "lessOrEquals", 1, 1),
            Scenario(FilterType.BIGGER_THAN, "biggerThan", 1, 1),
            Scenario(FilterType.BIGGER_OR_EQUALS, "biggerOrEquals", 1, 1),
            Scenario(FilterType.BETWEEN, "between", 2, 2),
            Scenario(FilterType.NOT_BETWEEN, "notBetween", 2, 2),
            Scenario(FilterType.IN, "in", 1, 99),
            Scenario(FilterType.NOT_IN, "notIn", 1, 99),
            Scenario(FilterType.LIKE, "like", 1, 1),
            Scenario(FilterType.NOT_LIKE, "notLike", 1, 1),
            Scenario(FilterType.IS_NULL, "isNull", 0, 0),
            Scenario(FilterType.IS_NOT_NULL, "isNotNull", 0, 0),
            Scenario(FilterType.IS_EMPTY, "isEmpty", 0, 0),
            Scenario(FilterType.IS_NOT_EMPTY, "isNotEmpty", 0, 0)
        )
    }

    @Test
    fun `it should have expected minimum parameters`() {
        // scenario
        val (type, _, expectedMinimum, _) = scenario

        // validation
        assertEquals(expectedMinimum, type.minimumParameters)
    }

    @Test
    fun `it should have expected maximum parameters`() {
        // scenario
        val (type, _, _, expectedMaximum) = scenario

        // validation
        assertEquals(expectedMaximum, type.maximumParameters)
    }

    @Test
    fun `it should be able to be located using identifier`() {
        // scenario
        val (type, identifier, _, _) = scenario

        // execution
        val result = FilterType.fromIdentifier(identifier)

        // validation
        assertEquals(type, result)
    }
}