package br.com.dillmann.restdb.core.filterDsl.converter

import br.com.dillmann.restdb.core.filterDsl.jdbc.filter.FilterJdbcPredicate
import br.com.dillmann.restdb.core.filterDsl.jdbc.group.GroupJdbcPredicate
import br.com.dillmann.restdb.testUtils.expect
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class TreeNodeCompilerUnitTests {

    @get:Rule
    val expectedException: ExpectedException = ExpectedException.none()

    @Test
    fun `when node has logical operator type, it should throw an error`() {
        // scenario
        val node = randomLogicalOperatorNode()

        // validation
        expectedException
            .expect<IllegalStateException>("Logical operators should be compiled indirectly by groups")

        // execution
        TreeNodeCompiler.compile(node)
    }

    @Test
    fun `it should be able to compile nodes with root type`() {
        // scenario
        val node = randomRootNode()

        // execution
        val result = TreeNodeCompiler.compile(node)

        // validation
        assertNotNull(result)
    }

    @Test
    fun `it should be able to compile nodes with group type`() {
        // scenario
        val node = randomGroupNode()

        // execution
        val result = TreeNodeCompiler.compile(node)

        // validation
        assertTrue { result is GroupJdbcPredicate }
    }

    @Test
    fun `it should be able to compile nodes with filter type`() {
        // scenario
        val node = randomFilterNode()

        // execution
        val result = TreeNodeCompiler.compile(node)

        // validation
        assertTrue { result is FilterJdbcPredicate }
    }
}