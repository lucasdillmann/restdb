package br.com.dillmann.restdb.core.filterDsl.jdbc.filter

import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate

/**
 * [JdbcPredicate] specialization for filter operations
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
interface FilterJdbcPredicate : JdbcPredicate {
    /**
     * Column name used by the filter operation
     */
    val columnName: String

    /**
     * Columns used by [sqlInstructions]
     */
    override val columns: Set<String>
        get() = setOf(columnName)
}
