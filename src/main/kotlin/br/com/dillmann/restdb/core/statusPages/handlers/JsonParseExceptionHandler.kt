package br.com.dillmann.restdb.core.statusPages.handlers

import br.com.dillmann.restdb.core.statusPages.logger
import br.com.dillmann.restdb.core.statusPages.utils.sendErrorResponse
import com.google.gson.JsonParseException
import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode

/**
 * Status page error handler for [JsonParseException]
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun StatusPages.Configuration.jsonParserExceptionHandler() {
    exception<JsonParseException> { exception ->
        logger.error(exception.message, exception)
        call.sendErrorResponse(HttpStatusCode.BadRequest, "Request body is not a valid JSON")
    }
}