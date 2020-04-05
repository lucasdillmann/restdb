package br.com.dillmann.restdb.domain.metadata

import io.ktor.application.ApplicationCall
import io.ktor.response.respond

/**
 * Handles GET requests for database metadata
 *
 * @param call Application call
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-01
 */
suspend fun handleGetMetadata(call: ApplicationCall) {
    val metadata = findDatabaseMetadata()
    call.respond(metadata)
}