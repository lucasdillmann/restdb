package br.com.dillmann.restdb.core.filterDsl.converter

import br.com.dillmann.restdb.core.filterDsl.jdbc.filter.FilterType
import br.com.dillmann.restdb.core.filterDsl.jdbc.logicalOperator.LogicalOperatorType
import br.com.dillmann.restdb.testUtils.randomString

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun randomFilterNode(): TreeNode =
    TreeNode(NodeType.FILTER, null).also {
        val type = FilterType.values().random()
        it.columnName = randomString()
        it.operation = type.identifier
        it.parameters = (0 until type.minimumParameters).map { randomString() }
    }

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun randomLogicalOperatorNode(): TreeNode =
    TreeNode(NodeType.LOGICAL_OPERATOR, null).also {
        it.logicalOperator = LogicalOperatorType.values().random().identifier
    }

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun randomGroupNode(): TreeNode =
    TreeNode(NodeType.GROUP, null).also {
        it.children += randomFilterNode()
        it.children += randomLogicalOperatorNode()
        it.children += randomFilterNode()
    }

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun randomRootNode(): TreeNode =
    TreeNode(NodeType.ROOT, null).also {
        it.children += randomFilterNode()
        it.children += randomLogicalOperatorNode()
        it.children += randomFilterNode()
        it.children += randomLogicalOperatorNode()
        it.children += randomGroupNode()
    }