package br.com.dillmann.restdb.domain.metadata

import io.ktor.application.call
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route

/**
 * Routes definition for metadata endpoints
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
fun Routing.metadataEndpoints() {
    route("/metadata") {
        get {
            handleGetMetadata(call)
        }
    }
}