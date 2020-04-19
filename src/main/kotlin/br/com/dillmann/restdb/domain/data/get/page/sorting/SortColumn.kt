package br.com.dillmann.restdb.domain.data.get.page.sorting

/**
 * Sorting parameters
 *
 * @param columnName Column name
 * @param direction Sorting direction (ASC or DESC)
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
data class SortColumn(
    val columnName: String,
    val direction: SortDirection
)