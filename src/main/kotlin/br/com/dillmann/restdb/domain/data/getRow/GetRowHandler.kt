package br.com.dillmann.restdb.domain.data.getRow

import br.com.dillmann.restdb.domain.data.utils.mainRequestParameters
import br.com.dillmann.restdb.domain.data.utils.rowId
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

/**
 * Handles GET requests for a single row in database
 *
 * @param call Application call
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
suspend fun handleGetRow(call: ApplicationCall) {
    val (partitionName, tableName) = call.mainRequestParameters()
    val rowId = call.rowId()

    findRow(partitionName, tableName, rowId)
        ?.let { call.respond(HttpStatusCode.OK, it) }
        ?: call.respond(HttpStatusCode.NotFound)
}
