package br.com.dillmann.restdb.core.statusPages.handlers

import br.com.dillmann.restdb.core.filterDsl.FilterDslSyntaxException
import br.com.dillmann.restdb.core.statusPages.utils.sendErrorResponse
import io.ktor.application.call
import io.ktor.features.StatusPages

/**
 * Status page error handler for [FilterDslSyntaxException]
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun StatusPages.Configuration.filterDslSyntaxExceptionHandler() {
    exception<FilterDslSyntaxException> { exception ->
        val details = mapOf(
            "line" to exception.line,
            "position" to exception.position,
            "symbol" to exception.symbol?.toString(),
            "description" to exception.description
        )

        call.sendErrorResponse(exception.statusCode, exception.message!!, details)
    }
}