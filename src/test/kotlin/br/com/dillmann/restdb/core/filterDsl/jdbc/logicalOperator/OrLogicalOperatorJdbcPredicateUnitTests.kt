package br.com.dillmann.restdb.core.filterDsl.jdbc.logicalOperator

import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate
import br.com.dillmann.restdb.testUtils.randomString
import br.com.dillmann.restdb.testUtils.randomUuid
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
class OrLogicalOperatorJdbcPredicateUnitTests {

    private val leftPredicate: JdbcPredicate = mockk()
    private val rightPredicate: JdbcPredicate = mockk()
    private lateinit var predicate: OrLogicalOperatorJdbcPredicate

    @Before
    fun setUp() {
        every { leftPredicate.sqlInstructions } returns randomString()
        every { leftPredicate.parameters } returns emptyMap()
        every { leftPredicate.columns } returns emptySet()
        every { rightPredicate.sqlInstructions } returns randomString()
        every { rightPredicate.parameters } returns emptyMap()
        every { rightPredicate.columns } returns emptySet()
    }

    @Test
    fun `sql instructions should contain left and right predicates instructions joined using an OR`() {
        // scenario
        val leftSql = randomString()
        val rightSql = randomString()
        val expectedResult = "$leftSql OR $rightSql"
        every { leftPredicate.sqlInstructions } returns leftSql
        every { rightPredicate.sqlInstructions } returns rightSql
        predicate = OrLogicalOperatorJdbcPredicate(leftPredicate, rightPredicate)

        // validation
        val result = predicate.sqlInstructions

        // validation
        assertEquals(expectedResult, result)
    }

    @Test
    fun `column names should contain left and right predicates column names`() {
        // scenario
        val leftColumnName = randomString()
        val rightColumnName = randomString()
        val expectedResult = setOf(leftColumnName, rightColumnName)
        every { leftPredicate.columns } returns setOf(leftColumnName)
        every { rightPredicate.columns } returns setOf(rightColumnName)
        predicate = OrLogicalOperatorJdbcPredicate(leftPredicate, rightPredicate)

        // validation
        val result = predicate.columns

        // validation
        assertEquals(expectedResult, result)
    }

    @Test
    fun `parameters should contain left and right predicates parameters`() {
        // scenario
        val leftParameters = mapOf(randomString() to randomUuid())
        val rightParameters = mapOf(randomString() to randomUuid())
        val expectedResult = leftParameters + rightParameters
        every { leftPredicate.parameters } returns leftParameters
        every { rightPredicate.parameters } returns rightParameters
        predicate = OrLogicalOperatorJdbcPredicate(leftPredicate, rightPredicate)

        // validation
        val result = predicate.parameters

        // validation
        assertEquals(expectedResult, result)
    }
}