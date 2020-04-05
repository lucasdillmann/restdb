package br.com.dillmann.restdb.core.filterDsl.jdbc.logicalOperator

import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate

/**
 * [JdbcPredicate] specialization for logical operators
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
interface LogicalOperatorJdbcPredicate : JdbcPredicate {

    /**
     * Left [JdbcPredicate] in the logical operator
     */
    val leftPredicate: JdbcPredicate

    /**
     * Right [JdbcPredicate] in the logical operator
     */
    val rightPredicate: JdbcPredicate

    /**
     * Columns used by [sqlInstructions]
     */
    override val columns: Set<String>
        get() = leftPredicate.columns + rightPredicate.columns

    /**
     * SQL dynamic parameters names and values
     */
    override val parameters: Map<String, Any>
        get() = leftPredicate.parameters + rightPredicate.parameters
}





