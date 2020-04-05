package br.com.dillmann.restdb.domain.data.delete

import br.com.dillmann.restdb.domain.data.utils.mainRequestParameters
import br.com.dillmann.restdb.domain.data.utils.rowId
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

/**
 * Handles DELETE requests for a single row in database
 *
 * @param call Application call
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
suspend fun handleDelete(call: ApplicationCall) {
    val (schemaName, tableName) = call.mainRequestParameters()
    val rowId = call.rowId()

    deleteRow(schemaName, tableName, rowId)
    call.respond(HttpStatusCode.NoContent)
}