package br.com.dillmann.restdb.core.filterDsl.jdbc.filter

import br.com.dillmann.restdb.core.filterDsl.utils.randomParameterId

/**
 * [FilterJdbcPredicate] implementation for SQL BETWEEN instruction
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
class BetweenFilterJdbcPredicate(
    /**
     * Column name used by the filter operation
     */
    override val columnName: String,

    /**
     * Left value to be compared with
     */
    leftValue: Any,

    /**
     * Right value to be compared with
     */
    rightValue: Any
) : FilterJdbcPredicate {
    private val leftParameterId = randomParameterId()
    private val rightParameterId = randomParameterId()

    /**
     * SQL instructions
     */
    override val sqlInstructions = "$columnName BETWEEN :$leftParameterId AND :$rightParameterId"

    /**
     * SQL dynamic parameters names and values
     */
    override val parameters = mapOf(
        leftParameterId to leftValue,
        rightParameterId to rightValue
    )
}