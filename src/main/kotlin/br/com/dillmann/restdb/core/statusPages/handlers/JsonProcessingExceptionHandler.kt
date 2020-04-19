package br.com.dillmann.restdb.core.statusPages.handlers

import br.com.dillmann.restdb.core.statusPages.logger
import br.com.dillmann.restdb.core.statusPages.utils.sendErrorResponse
import com.fasterxml.jackson.core.JsonProcessingException
import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode

/**
 * Status page error handler for [JsonProcessingException]
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun StatusPages.Configuration.jsonProcessingExceptionHandler() {
    exception<JsonProcessingException> { exception ->
        logger.error(exception.message, exception)
        val details = mapOf(
            "line" to exception.location.lineNr,
            "column" to exception.location.columnNr,
            "charOffset" to exception.location.charOffset,
            "message" to exception.message
        )
        call.sendErrorResponse(
            statusCode = HttpStatusCode.BadRequest,
            message = "Request body is not a valid JSON",
            details = details
        )
    }
}