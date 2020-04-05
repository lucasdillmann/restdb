package br.com.dillmann.restdb.core.filterDsl.jdbc.filter

import br.com.dillmann.restdb.testUtils.randomString
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
class IsEmptyFilterJdbcPredicateUnitTests {

    private val columnName = randomString()
    private val predicate = IsEmptyFilterJdbcPredicate(columnName)

    @Test
    fun `output sql should use equals instruction`() {
        // execution
        val sql = predicate.sqlInstructions

        // validation
        assertTrue { sql.contains("=", ignoreCase = true) }
    }

    @Test
    fun `output sql should use column name`() {
        // execution
        val sql = predicate.sqlInstructions

        // validation
        assertTrue { sql.contains(columnName, ignoreCase = true) }
    }

    @Test
    fun `output sql should have one parameter`() {
        // scenario
        val paramPattern = ":param_[a-zA-Z0-9]+".toRegex()

        // execution
        val sql = predicate.sqlInstructions

        // validation
        assertEquals(1, paramPattern.findAll(sql).count())
    }

    @Test
    fun `output parameters should have values to keys used in output sql`() {
        // scenario
        val paramPattern = ":(param_[a-zA-Z0-9]+)".toRegex()

        // execution
        val sql = predicate.sqlInstructions
        val parameters = predicate.parameters

        // validation
        val groups = paramPattern.findAll(sql).map { it.value.drop(1) }
        assertTrue { groups.all(parameters::containsKey) }
    }

    @Test
    fun `it should have an empty value in parameters`() {
        // execution
        val parameters = predicate.parameters

        // validation
        assertEquals("", parameters.values.firstOrNull())
    }
}