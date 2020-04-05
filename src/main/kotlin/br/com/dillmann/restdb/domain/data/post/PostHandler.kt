package br.com.dillmann.restdb.domain.data.post

import br.com.dillmann.restdb.domain.data.utils.jsonBody
import br.com.dillmann.restdb.domain.data.utils.mainRequestParameters
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

/**
 * Handles POST requests to create new rows in database
 *
 * @param call Application call
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
suspend fun handlePost(call: ApplicationCall) {
    val (schemaName, tableName) = call.mainRequestParameters()
    val body = call.jsonBody()

    val createdRow = createRow(body, schemaName, tableName)
    call.respond(HttpStatusCode.Created, createdRow)
}