package br.com.dillmann.restdb.core.filterDsl.jdbc.filter

import br.com.dillmann.restdb.testUtils.randomString
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
class NotBetweenFilterJdbcPredicateUnitTests {

    private val leftValue = randomString()
    private val rightValue = randomString()
    private val columnName = randomString()
    private val predicate = NotBetweenFilterJdbcPredicate(columnName, leftValue, rightValue)

    @Test
    fun `output sql should use not between instruction`() {
        // execution
        val sql = predicate.sqlInstructions

        // validation
        assertTrue { sql.contains("NOT BETWEEN", ignoreCase = true) }
    }

    @Test
    fun `output sql should use column name`() {
        // execution
        val sql = predicate.sqlInstructions

        // validation
        assertTrue { sql.contains(columnName, ignoreCase = true) }
    }

    @Test
    fun `output sql should have two parameters`() {
        // scenario
        val paramPattern = ":param_[a-zA-Z0-9]+".toRegex()

        // execution
        val sql = predicate.sqlInstructions

        // validation
        assertEquals(2, paramPattern.findAll(sql).count())
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
    fun `it should have left value in parameters`() {
        // execution
        val parameters = predicate.parameters

        // validation
        assertTrue { leftValue in parameters.values }
    }

    @Test
    fun `it should have right value in parameters`() {
        // execution
        val parameters = predicate.parameters

        // validation
        assertTrue { rightValue in parameters.values }
    }
}