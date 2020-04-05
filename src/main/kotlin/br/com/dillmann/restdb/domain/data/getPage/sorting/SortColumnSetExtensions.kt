package br.com.dillmann.restdb.domain.data.getPage.sorting

/**
 * Converts a [Set] of [SortColumn] into sorting SQL intructions
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
fun Set<SortColumn>.asSqlInstructions(): String {
    val columns = joinToString { "${it.columnName} ${it.direction}" }
    return "ORDER BY $columns"
}