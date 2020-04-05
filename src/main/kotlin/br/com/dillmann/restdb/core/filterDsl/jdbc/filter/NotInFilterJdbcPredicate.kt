package br.com.dillmann.restdb.core.filterDsl.jdbc.filter

import br.com.dillmann.restdb.core.filterDsl.utils.randomParameterId

/**
 * [FilterJdbcPredicate] implementation for SQL NOT IN instruction
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
class NotInFilterJdbcPredicate(
    /**
     * Column name used by the filter operation
     */
    override val columnName: String,

    /**
     * Values to be compared with
     */
    private val values: List<Any>
) : FilterJdbcPredicate {

    private val parameterIds = values.map { randomParameterId() }

    /**
     * SQL instructions
     */
    override val sqlInstructions =
        parameterIds.joinToString(separator = " AND ", prefix = "(", postfix = ")") { "$columnName != :$it" }

    /**
     * SQL dynamic parameters names and values
     */
    override val parameters =
        parameterIds.mapIndexed { index, parameterName -> parameterName to values[index] }.toMap()
}