package br.com.dillmann.restdb.core.statusPages.utils

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.uri
import io.ktor.response.respond

/**
 * Generates and writes a HTTP response using provided parameters
 *
 * @param statusCode HTTP status code
 * @param message Response main message
 * @param details Response optional details
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
suspend fun ApplicationCall.sendErrorResponse(
    statusCode: HttpStatusCode,
    message: String,
    details: Any? = null
) {
    val responseBody = mapOf(
        "statusCode" to statusCode.value,
        "statusDescription" to statusCode.description,
        "uri" to request.uri,
        "message" to message,
        "details" to details
    )

    respond(statusCode, responseBody)
}