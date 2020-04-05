package br.com.dillmann.restdb.core.filterDsl.jdbc.group

import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate
import br.com.dillmann.restdb.testUtils.randomString
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
class GroupJdbcPredicateUnitTests {

    private val childPredicate: JdbcPredicate = mockk()
    private lateinit var predicate: GroupJdbcPredicate

    @Before
    fun setUp() {
        every { childPredicate.sqlInstructions } returns randomString()
        every { childPredicate.parameters } returns emptyMap()
        every { childPredicate.columns } returns emptySet()
    }

    @Test
    fun `it should encapsulate child sql instructions under parenthesis`() {
        // scenario
        val childSql = randomString()
        val expectedSql = "($childSql)"
        every { childPredicate.sqlInstructions } returns childSql
        predicate = GroupJdbcPredicate(childPredicate)

        // execution
        val result = predicate.sqlInstructions

        // validation
        assertEquals(expectedSql, result)
    }

    @Test
    fun `it should delegate parameters resolution to child`() {
        // scenario
        val expectedParameters = mapOf(randomString() to randomString())
        every { childPredicate.parameters } returns expectedParameters
        predicate = GroupJdbcPredicate(childPredicate)

        // execution
        val result = predicate.parameters

        // validation
        assertEquals(expectedParameters, result)
    }

    @Test
    fun `it should delegate column names resolution to child`() {
        // scenario
        val expectedNames = setOf(randomString(), randomString())
        every { childPredicate.columns } returns expectedNames
        predicate = GroupJdbcPredicate(childPredicate)

        // execution
        val result = predicate.columns

        // validation
        assertEquals(expectedNames, result)
    }
}