package br.com.dillmann.restdb.core.filterDsl.converter

import br.com.dillmann.restdb.core.filterDsl.converter.NodeType.GROUP
import br.com.dillmann.restdb.core.filterDsl.converter.NodeType.ROOT
import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate

/**
 * Conversion tree node definition
 *
 * This class stores the metadata read using ANTLR parser during the conversion. When parse is completed,
 * the metadata stored here can be used to produce a [JdbcPredicate].
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-01-30
 */
class TreeNode(var type: NodeType, val parent: TreeNode?) {
    val children = mutableListOf<TreeNode>()
    var operation: String? = null
    var columnName: String? = null
    var parameters: List<Any>? = null
    var logicalOperator: String? = null

    /**
     * Stores the provided [node] as a child of this node
     * @throws IllegalArgumentException when current node type is not allowed to have children
     */
    fun addChild(node: TreeNode) {
        check(type in listOf(GROUP, ROOT)) { "Nodes of type $type cannot have children" }
        children.add(node)
    }
}