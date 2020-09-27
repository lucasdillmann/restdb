package br.com.dillmann.restdb.core.filterDsl.converter

import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate

/**
 * Filter DSL conversion context
 *
 * This class stores the temporary conversion tree where each node represents an expression (filter), a group
 * of expressions or a logical operator. When tree is ready, it could then be compiled to a [JdbcPredicate].
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-30
 */
class ConversionContext() {
    val root = TreeNode(NodeType.ROOT, null)
    var currentNode = root
        private set

    /**
     * Starts a node in the conversion tree with the given [type]
     * @throws IllegalArgumentException when [type] is [NodeType.ROOT]
     */
    fun startNode(type: NodeType) {
        require(type != NodeType.ROOT) { "Cannot create child nodes of type ROOT" }
        val newNode = TreeNode(type, currentNode)
        currentNode.addChild(newNode)
        currentNode = newNode
    }

    /**
     * Closes current node in the conversion tree and move pointer to its parent node
     * @throws IllegalStateException when current node is the root node, which cannot be ended
     */
    fun endNode() {
        check(currentNode.parent != null) { "Cannot close root node" }
        currentNode = currentNode.parent!!
    }
}