package br.com.dillmann.restdb.core.filterDsl.jdbc

import br.com.dillmann.restdb.core.filterDsl.jdbc.exception.InvalidParameterCountException
import br.com.dillmann.restdb.core.filterDsl.jdbc.filter.FilterType
import br.com.dillmann.restdb.core.filterDsl.jdbc.logicalOperator.LogicalOperatorType
import br.com.dillmann.restdb.testUtils.expect
import br.com.dillmann.restdb.testUtils.randomString
import io.mockk.mockk
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Suite
import kotlin.test.assertNotNull

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(value = [
    JdbcPredicateFactoryUnitTests.Filter::class,
    JdbcPredicateFactoryUnitTests.LogicalOperator::class,
    JdbcPredicateFactoryUnitTests.Group::class
])
class JdbcPredicateFactoryUnitTests {

    @RunWith(Parameterized::class)
    class Filter(private val type: FilterType) {
        companion object {
            @JvmStatic
            @get:Parameterized.Parameters(name = "{0}")
            val data = FilterType.values()
        }

        @get:Rule
        val expectedException: ExpectedException = ExpectedException.none()

        @Test
        fun `it should be able to create a filter instance`() {
            // scenario
            val parameters = (0 until type.minimumParameters).map { randomString() }

            // execution
            val predicate = JdbcPredicateFactory.filter(type.identifier, randomString(), parameters)

            // validation
            assertNotNull(predicate)
        }

        @Test
        fun `it should throw an error when has less parameters than expected`() {
            // conditional execution
            assumeTrue(type.minimumParameters > 0)

            // scenario
            val parameters = emptyList<String>()
            val columnName = randomString()
            val discriminator = if (type.minimumParameters == type.maximumParameters) "exactly" else "at least"
            val expectedMessage = "Invalid parameters on filter ${type.identifier} of column $columnName. " +
                    "Token has 0 value(s) but it expects $discriminator ${type.minimumParameters} value(s)."

            // validation
            expectedException.expect<InvalidParameterCountException>(expectedMessage)

            // execution
            JdbcPredicateFactory.filter(type.identifier, columnName, parameters)
        }

        @Test
        fun `it should throw an error when has more parameters than expected`() {
            // scenario
            val parameters = (0..type.maximumParameters + 1).map { randomString() }
            val columnName = randomString()
            val discriminator = if (type.minimumParameters == type.maximumParameters) "exactly" else "less than"
            val expectedMessage = "Invalid parameters on filter ${type.identifier} of column $columnName. " +
                    "Token has ${parameters.size} value(s) but it expects $discriminator ${type.maximumParameters} value(s)."

            // validation
            expectedException.expect<InvalidParameterCountException>(expectedMessage)

            // execution
            JdbcPredicateFactory.filter(type.identifier, columnName, parameters)
        }
    }

    @RunWith(Parameterized::class)
    class LogicalOperator(private val type: LogicalOperatorType) {
        companion object {
            @JvmStatic
            @get:Parameterized.Parameters(name = "{0}")
            val data = LogicalOperatorType.values()
        }

        @Test
        fun `it should be able to create a logical operator instance`() {
            // scenario
            val leftPredicate: JdbcPredicate = mockk(relaxed = true)
            val rightPredicate: JdbcPredicate = mockk(relaxed = true)

            // execution
            val predicate = JdbcPredicateFactory.logicalOperator(type.identifier, leftPredicate, rightPredicate)

            // validation
            assertNotNull(predicate)
        }

    }

    class Group {

        @Test
        fun `it should be able to produce group instances`() {
            // scenario
            val childPredicate: JdbcPredicate = mockk(relaxed = true)

            // execution
            val predicate = JdbcPredicateFactory.group(childPredicate)

            // validation
            assertNotNull(predicate)
        }
    }
}