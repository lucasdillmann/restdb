package br.com.dillmann.restdb.domain.data.get.page.sorting

import br.com.dillmann.restdb.domain.data.get.page.exception.InvalidSortingDirectionException

/**
 * Converts a [String] into a [Set] of [SortColumn]
 *
 * Conversion is done by spitting String using comma as separator. Each segment is then parsed using the
 * format <columnName>:<sortDirection>, where the sorting direction is optional.
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
fun String.asSortingInstructions(): Set<SortColumn>? {
    if (isBlank()) return null
    return split(",").map { it.asSortElement() }.toSet()
}

/**
 * Converts a [String] into a [SortColumn]
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
private fun String.asSortElement(): SortColumn {
    val parts = split(":")
    val direction = parts
        .takeIf { it.size > 1 }
        ?.get(1)
        ?.asSortDirection(parts[0])
        ?: SortDirection.ASC

    return SortColumn(
        columnName = parts[0],
        direction = direction
    )
}

/**
 * Converts a [String] into a [SortDirection]
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
private fun String.asSortDirection(columnName: String): SortDirection =
    try {
        SortDirection.valueOf(toUpperCase())
    } catch (ex: IllegalArgumentException) {
        throw InvalidSortingDirectionException(
            columnName,
            this
        )
    }