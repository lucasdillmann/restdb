package br.com.dillmann.restdb.core.filterDsl.jdbc.exception

import br.com.dillmann.restdb.core.statusPages.exceptions.BadRequestException

/**
 * Exception definition for Filter DSL expression with invalid number of parameters
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
class InvalidParameterCountException(
    filter: String,
    columnName: String,
    current: Int,
    expected: Int,
    discriminator: String
) : BadRequestException(
    "Invalid parameters on filter $filter of column $columnName. " +
            "Token has $current value(s) but it expects $discriminator $expected value(s)."
)