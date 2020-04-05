package br.com.dillmann.restdb.core.filterDsl.jdbc.filter

import br.com.dillmann.restdb.core.filterDsl.utils.randomParameterId

/**
 * [FilterJdbcPredicate] implementation for SQL NOT EMPTY instruction
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
class IsNotEmptyFilterJdbcPredicate(
    /**
     * Column name used by the filter operation
     */
    override val columnName: String
) : FilterJdbcPredicate {
    private val parameterId = randomParameterId()

    /**
     * SQL instructions
     */
    override val sqlInstructions = "$columnName != :$parameterId"

    /**
     * SQL dynamic parameters names and values
     */
    override val parameters = mapOf(parameterId to "")
}