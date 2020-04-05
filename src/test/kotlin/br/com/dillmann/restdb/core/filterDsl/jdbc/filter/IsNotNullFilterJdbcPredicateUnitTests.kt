package br.com.dillmann.restdb.core.filterDsl.jdbc.filter

import br.com.dillmann.restdb.testUtils.randomString
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
class IsNotNullFilterJdbcPredicateUnitTests {

    private val columnName = randomString()
    private val predicate = IsNotNullFilterJdbcPredicate(columnName)

    @Test
    fun `output sql should use is null instruction`() {
        // execution
        val sql = predicate.sqlInstructions

        // validation
        assertTrue { sql.contains("IS NOT NULL", ignoreCase = true) }
    }

    @Test
    fun `output sql should use column name`() {
        // execution
        val sql = predicate.sqlInstructions

        // validation
        assertTrue { sql.contains(columnName, ignoreCase = true) }
    }

    @Test
    fun `output sql should not have parameters`() {
        // scenario
        val paramPattern = ":param_[a-zA-Z0-9]+".toRegex()

        // execution
        val sql = predicate.sqlInstructions

        // validation
        assertEquals(0, paramPattern.findAll(sql).count())
    }

}