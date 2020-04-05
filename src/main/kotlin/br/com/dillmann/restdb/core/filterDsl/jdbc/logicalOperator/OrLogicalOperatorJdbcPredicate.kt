package br.com.dillmann.restdb.core.filterDsl.jdbc.logicalOperator

import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate

/**
 * [LogicalOperatorJdbcPredicate] implementation for SQL OR
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
class OrLogicalOperatorJdbcPredicate(
    /**
     * Left [JdbcPredicate] in the logical operator
     */
    override val leftPredicate: JdbcPredicate,

    /**
     * Right [JdbcPredicate] in the logical operator
     */
    override val rightPredicate: JdbcPredicate
) : LogicalOperatorJdbcPredicate {

    /**
     * SQL instructions
     */
    override val sqlInstructions = "${leftPredicate.sqlInstructions} OR ${rightPredicate.sqlInstructions}"
}