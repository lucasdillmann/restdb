package br.com.dillmann.restdb.domain.index

import io.ktor.application.call
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route

/**
 * Routes definition for index endpoints
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-25
 */
fun Routing.indexEndpoints() {
    route("/") {
        get {
            handleGetIndex(call)
        }
    }
}