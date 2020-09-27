package br.com.dillmann.restdb.core.filterDsl.converter

import br.com.dillmann.restdb.core.filterDsl.FilterDslBaseListener
import br.com.dillmann.restdb.core.filterDsl.FilterDslParser
import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate

/**
 * ANTLR [FilterDslBaseListener] implementation able to convert ANTLR tree to application [JdbcPredicate]
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-30
 */
class FilterDslJdbcConverter(private val partitionName: String, private val tableName: String) : FilterDslBaseListener() {
    private val context = ConversionContext()

    /**
     * Compiles current conversion to a [JdbcPredicate]
     */
    fun jdbcPredicate() =
        TreeNodeCompiler(partitionName, tableName, context.root).compile()

    /**
     * Notifies conversion listener that ANTLR parser is entering an expression group
     */
    override fun enterGroup(parserContext: FilterDslParser.GroupContext) {
        context.startNode(NodeType.GROUP)
    }

    /**
     * Notifies conversion listener that ANTLR parser is exiting an expression group
     */
    override fun exitGroup(parserContext: FilterDslParser.GroupContext) {
        context.endNode()
    }

    /**
     * Notifies conversion listener that ANTLR parser is entering an expression
     */
    override fun enterExpression(parserContext: FilterDslParser.ExpressionContext) {
        context.startNode(NodeType.FILTER)
    }

    /**
     * Notifies conversion listener that ANTLR parser is exiting an expression
     */
    override fun exitExpression(parserContext: FilterDslParser.ExpressionContext) {
        context.endNode()
    }

    /**
     * Notifies conversion listener that ANTLR parser has read an expression column name
     */
    override fun exitColumnName(parserContext: FilterDslParser.ColumnNameContext) {
        context.currentNode.columnName = parserContext.text
    }

    /**
     * Notifies conversion listener that ANTLR parser has read an expression operation
     */
    override fun exitOperation(parserContext: FilterDslParser.OperationContext) {
        context.currentNode.operation = parserContext.text
    }

    /**
     * Notifies conversion listener that ANTLR parser is entering expression parameters
     */
    override fun enterParameters(parserContext: FilterDslParser.ParametersContext) {
        context.currentNode.parameters = emptyList()
    }

    /**
     * Notifies conversion listener that ANTLR parser has read an expression string parameter
     */
    override fun exitParameterStringValue(parserContext: FilterDslParser.ParameterStringValueContext) {
        context.currentNode.parameters =
            context.currentNode.parameters!! + parserContext.text.removeSurrounding("\"")
    }

    /**
     * Notifies conversion listener that ANTLR parser has read an expression numeric parameter
     */
    override fun exitParameterNumericValue(parserContext: FilterDslParser.ParameterNumericValueContext) {
        context.currentNode.parameters =
            context.currentNode.parameters!! + parserContext.text
    }

    /**
     * Notifies conversion listener that ANTLR parser has read a logical operator
     */
    override fun exitLogicalOperator(parserContext: FilterDslParser.LogicalOperatorContext) {
        context.startNode(NodeType.LOGICAL_OPERATOR)
        context.currentNode.logicalOperator = parserContext.text
        context.endNode()
    }
}