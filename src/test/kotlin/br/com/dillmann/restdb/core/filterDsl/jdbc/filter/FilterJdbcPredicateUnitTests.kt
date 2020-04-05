package br.com.dillmann.restdb.core.filterDsl.jdbc.filter

import br.com.dillmann.restdb.testUtils.randomString
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
class FilterJdbcPredicateUnitTests {

    private val randomColumnName = randomString()
    private val predicate = object : FilterJdbcPredicate {
        override val columnName = randomColumnName
        override val sqlInstructions: String
            get() = error("Not in the context of this test class")
        override val parameters: Map<String, Any>
            get() = error("Not in the context of this test class")
    }

    @Test
    fun `it should return column name in the list of column names`() {
        // execution
        val result = predicate.columns

        // validation
        assertEquals(1, result.size)
        assertEquals(randomColumnName, result.firstOrNull())
    }
}