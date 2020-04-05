package br.com.dillmann.restdb.core.filterDsl.converter

import br.com.dillmann.restdb.testUtils.expect
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import kotlin.test.assertTrue

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
class TreeNodeUnitTests {

    @get:Rule
    val expectedException: ExpectedException = ExpectedException.none()

    @Test
    fun `nodes with FILTER type should not allow to add a child`() {
        // scenario
        val node = TreeNode(NodeType.FILTER, null)

        // validation
        expectedException.expect<IllegalStateException>("Nodes of type FILTER cannot have children")

        // execution
        node.addChild(mockk())
    }

    @Test
    fun `nodes with LOGICAL_OPERATOR type should not allow to add a child`() {
        // scenario
        val node = TreeNode(NodeType.LOGICAL_OPERATOR, null)

        // validation
        expectedException.expect<IllegalStateException>("Nodes of type LOGICAL_OPERATOR cannot have children")

        // execution
        node.addChild(mockk())
    }

    @Test
    fun `nodes with GROUP type should allow to add a child`() {
        // scenario
        val child: TreeNode = mockk()
        val node = TreeNode(NodeType.GROUP, null)

        // execution
        node.addChild(child)

        // validation
        assertTrue { child in node.children }
    }

    @Test
    fun `nodes with ROOT type should allow to add a child`() {
        // scenario
        val child: TreeNode = mockk()
        val node = TreeNode(NodeType.ROOT, null)

        // execution
        node.addChild(child)

        // validation
        assertTrue { child in node.children }
    }
}