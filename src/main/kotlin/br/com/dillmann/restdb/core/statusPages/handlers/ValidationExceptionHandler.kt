package br.com.dillmann.restdb.core.statusPages.handlers

import br.com.dillmann.restdb.core.statusPages.exceptions.ValidationException
import br.com.dillmann.restdb.core.statusPages.utils.sendErrorResponse
import io.ktor.application.call
import io.ktor.features.StatusPages

/**
 * Status page error handler for [ValidationException]
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun StatusPages.Configuration.validationExceptionHandler() {
    exception<ValidationException> { exception ->
        call.sendErrorResponse(
            statusCode = exception.statusCode,
            message = exception.message!!,
            details = exception.details
        )
    }
}
