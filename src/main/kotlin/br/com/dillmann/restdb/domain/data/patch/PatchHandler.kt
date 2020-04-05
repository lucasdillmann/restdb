package br.com.dillmann.restdb.domain.data.patch

import br.com.dillmann.restdb.domain.data.utils.jsonBody
import br.com.dillmann.restdb.domain.data.utils.mainRequestParameters
import br.com.dillmann.restdb.domain.data.utils.rowId
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

/**
 * Handles PATCH requests for a single row in database
 *
 * @param call Application call
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
suspend fun handlePatch(call: ApplicationCall) {
    val (schemaName, tableName) = call.mainRequestParameters()
    val rowId = call.rowId()
    val body = call.jsonBody()

    val updatedRow = updateRow(body, schemaName, tableName, rowId)
    call.respond(HttpStatusCode.OK, updatedRow)
}