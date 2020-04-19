package br.com.dillmann.restdb.domain.data.get.page.exception

import br.com.dillmann.restdb.core.statusPages.exceptions.BadRequestException
import br.com.dillmann.restdb.domain.data.get.page.sorting.SortDirection

/**
 * [BadRequestException] specialization for user provided invalid sorting direction
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
class InvalidSortingDirectionException(columnName: String, direction: String) :
    BadRequestException(
        "Invalid sorting direction $direction for column $columnName. " +
                "Allowed values: ${SortDirection.values().joinToString()}."
    )