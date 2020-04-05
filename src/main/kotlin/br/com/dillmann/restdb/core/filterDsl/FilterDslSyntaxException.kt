package br.com.dillmann.restdb.core.filterDsl

import br.com.dillmann.restdb.core.statusPages.exceptions.BadRequestException

/**
 * Filter DSL syntax error definition exception
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-30
 */
class FilterDslSyntaxException(
    /**
     * Line number where the error was detected
     */
    val line: Int,
    /**
     * Position (char ordinal number) where the error was detected
     */
    val position: Int,
    /**
     * Symbol/expression where the error was detected
     */
    val symbol: Any?,
    /**
     * Error description and solution
     */
    val description: String?
) : BadRequestException("Filter syntax is invalid")
