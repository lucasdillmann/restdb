package br.com.dillmann.restdb.core.filterDsl.converter

import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate
import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicateFactory
import br.com.dillmann.restdb.core.filterDsl.jdbc.filter.FilterJdbcPredicate
import br.com.dillmann.restdb.core.filterDsl.jdbc.group.GroupJdbcPredicate
import br.com.dillmann.restdb.core.filterDsl.jdbc.logicalOperator.LogicalOperatorJdbcPredicate

/**
 * Compiles a [TreeNode] and his children into a [JdbcPredicate]
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-30
 */
object TreeNodeCompiler {

    /**
     * Starts the compilation
     *
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-03-30
     */
    fun compile(node: TreeNode): JdbcPredicate =
        when (node.type) {
            NodeType.ROOT -> compileChildren(node)
            NodeType.GROUP -> compileGroup(node)
            NodeType.FILTER -> compileFilter(node)
            NodeType.LOGICAL_OPERATOR -> error("Logical operators should be compiled indirectly by groups")
        }

    /**
     * Compiles a [TreeNode] into a [FilterJdbcPredicate]
     */
    private fun compileFilter(node: TreeNode) =
        JdbcPredicateFactory.filter(
            node.operation!!,
            node.columnName!!,
            node.parameters ?: emptyList()
        )

    /**
     * Compiles a [TreeNode] into a [GroupJdbcPredicate]
     */
    private fun compileGroup(node: TreeNode) =
        JdbcPredicateFactory.group(compileChildren(node))

    /**
     * Compiles a [TreeNode] into a [LogicalOperatorJdbcPredicate]
     */
    private fun compileLogicalOperator(node: TreeNode, left: JdbcPredicate, right: JdbcPredicate) =
        JdbcPredicateFactory.logicalOperator(node.logicalOperator!!, left, right)

    /**
     * Compiles [TreeNode] children into a [JdbcPredicate]
     */
    private fun compileChildren(parent: TreeNode): JdbcPredicate {
        var leftPredicate: JdbcPredicate? = null
        var logicalOperatorNode: TreeNode? = null

        parent.children.forEach { child ->
            if (child.type == NodeType.LOGICAL_OPERATOR) {
                logicalOperatorNode = child
            } else {
                val currentPredicate = compile(child)
                leftPredicate =
                    if (leftPredicate == null) currentPredicate
                    else compileLogicalOperator(logicalOperatorNode!!, leftPredicate!!, currentPredicate)
            }
        }

        return leftPredicate!!
    }
}