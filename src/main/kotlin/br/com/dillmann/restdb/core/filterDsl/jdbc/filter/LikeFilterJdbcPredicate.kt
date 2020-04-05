package br.com.dillmann.restdb.core.filterDsl.jdbc.filter

import br.com.dillmann.restdb.core.filterDsl.utils.randomParameterId

/**
 * [FilterJdbcPredicate] implementation for SQL LIKE instruction
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
class LikeFilterJdbcPredicate(
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
    override val sqlInstructions = "$columnName LIKE :$parameterId"

    /**
     * SQL dynamic parameters names and values
     */
    override val parameters = mapOf(parameterId to value)
}