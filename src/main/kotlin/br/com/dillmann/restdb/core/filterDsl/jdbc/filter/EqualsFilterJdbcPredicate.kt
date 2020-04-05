package br.com.dillmann.restdb.core.filterDsl.jdbc.filter

import br.com.dillmann.restdb.core.filterDsl.utils.randomParameterId

/**
 * [FilterJdbcPredicate] implementation for SQL = instruction
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
class EqualsFilterJdbcPredicate(
    /**
     * Column name used by the filter operation
     */
    override val columnName: String,

    /**
     * Value to be compared with
     */
    value: Any
) : FilterJdbcPredicate {
    private val parameterId = randomParameterId()

    /**
     * SQL instructions
     */
    override val sqlInstructions = "$columnName = :$parameterId"

    /**
     * SQL dynamic parameters names and values
     */
    override val parameters = mapOf(parameterId to value)
}