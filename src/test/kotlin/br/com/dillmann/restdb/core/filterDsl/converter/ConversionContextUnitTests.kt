package br.com.dillmann.restdb.core.filterDsl.converter

import br.com.dillmann.restdb.testUtils.expect
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
class ConversionContextUnitTests {

    @get:Rule
    val expectedException: ExpectedException = ExpectedException.none()
    private val context = ConversionContext()

    @Test
    fun `when asked to add a node with ROOT type it should throw an error`() {
        // scenario
        val type = NodeType.ROOT

        // validation
        expectedException.expect<IllegalArgumentException>("Cannot create child nodes of type ROOT")

        // execution
        context.startNode(type)
    }

    @Test
    fun `it should be able to create FILTER nodes`() {
        // scenario
        val type = NodeType.FILTER

        // execution
        context.startNode(type)

        // validation
        assertEquals(type, context.currentNode.type)
    }

    @Test
    fun `it should be able to create GROUP nodes`() {
        // scenario
        val type = NodeType.GROUP

        // execution
        context.startNode(type)

        // validation
        assertEquals(type, context.currentNode.type)
    }

    @Test
    fun `it should be able to create LOGICAL_OPERATOR nodes`() {
        // scenario
        val type = NodeType.LOGICAL_OPERATOR

        // execution
        context.startNode(type)

        // validation
        assertEquals(type, context.currentNode.type)
    }

    @Test
    fun `when asked to close ROOT node it should throw an error`() {
        // validation
        expectedException.expect<IllegalStateException>("Cannot close root node")

        // execution
        context.endNode()
    }

    @Test
    fun `it should be able to close FILTER nodes`() {
        // scenario
        context.startNode(NodeType.FILTER)

        // execution
        context.endNode()

        // validation
        assertTrue { context.currentNode.parent == null }
    }

    @Test
    fun `it should be able to close GROUP nodes`() {
        // scenario
        context.startNode(NodeType.GROUP)

        // execution
        context.endNode()

        // validation
        assertTrue { context.currentNode.parent == null }
    }

    @Test
    fun `it should be able to close LOGICAL_OPERATOR nodes`() {
        // scenario
        context.startNode(NodeType.LOGICAL_OPERATOR)

        // execution
        context.endNode()

        // validation
        assertTrue { context.currentNode.parent == null }
    }

}