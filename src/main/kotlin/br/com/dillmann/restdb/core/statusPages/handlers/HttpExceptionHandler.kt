package br.com.dillmann.restdb.core.statusPages.handlers

import br.com.dillmann.restdb.core.statusPages.exceptions.HttpException
import br.com.dillmann.restdb.core.statusPages.utils.sendErrorResponse
import io.ktor.application.call
import io.ktor.features.StatusPages

/**
 * Status page error handler for [HttpException]
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun StatusPages.Configuration.httpExceptionHandler() {
    exception<HttpException> { exception ->
        call.sendErrorResponse(exception.statusCode, exception.message!!)
    }
}