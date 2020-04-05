package br.com.dillmann.restdb.core.statusPages

import br.com.dillmann.restdb.core.statusPages.exceptions.MethodNotAllowedException
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.request.httpMethod
import io.ktor.routing.Routing
import io.ktor.routing.route

/**
 * Extension function for [Application] for Ktor default responses
 *
 * This function configures and installs a default response for any unmapped route in application.
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-26
 */
fun Routing.fallbackResponse() {
    route("/{...}") { // Fallback response
        handle {
            throw MethodNotAllowedException(
                call.request.httpMethod.value
            )
        }
    }
}