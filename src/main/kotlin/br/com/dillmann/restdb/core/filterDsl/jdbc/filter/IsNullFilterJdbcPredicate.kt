package br.com.dillmann.restdb.core.filterDsl.jdbc.filter

/**
 * [FilterJdbcPredicate] implementation for SQL IS NULL instruction
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
class IsNullFilterJdbcPredicate(
    /**
     * Column name used by the filter operation
     */
    override val columnName: String
) : FilterJdbcPredicate {

    /**
     * SQL instructions
     */
    override val sqlInstructions = "$columnName IS NULL"

    /**
     * SQL dynamic parameters names and values
     */
    override val parameters = emptyMap<String, Any>()
}