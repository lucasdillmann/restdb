package br.com.dillmann.restdb.core.filterDsl.jdbc.filter

import br.com.dillmann.restdb.core.filterDsl.utils.randomParameterId

/**
 * [FilterJdbcPredicate] implementation for SQL IN instruction
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
class InFilterJdbcPredicate(
    /**
     * Column name used by the filter operation
     */
    override val columnName: String,

    /**
     * Value to be compared with
     */
    private val values: List<Any>
) : FilterJdbcPredicate {

    private val parameterIds = values.map { randomParameterId() }

    /**
     * SQL instructions
     */
    override val sqlInstructions =
        parameterIds.joinToString(separator = " OR ", prefix = "(", postfix = ")") { "$columnName = :$it" }

    /**
     * SQL dynamic parameters names and values
     */
    override val parameters =
        parameterIds.mapIndexed { index, parameterName -> parameterName to values[index] }.toMap()

}