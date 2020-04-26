package br.com.dillmann.restdb

import br.com.dillmann.restdb.core.statusPages.fallbackResponse
import br.com.dillmann.restdb.core.statusPages.installErrorHandlers
import br.com.dillmann.restdb.core.installContentNegotiation
import br.com.dillmann.restdb.core.installCors
import br.com.dillmann.restdb.core.installRequestTracing
import br.com.dillmann.restdb.domain.data.dataEndpoints
import br.com.dillmann.restdb.domain.index.indexEndpoints
import br.com.dillmann.restdb.domain.metadata.metadataEndpoints
import io.ktor.application.Application
import io.ktor.routing.routing

/**
 * Application Ktor module definition
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
fun Application.module() {
    installContentNegotiation()
    installRequestTracing()
    installErrorHandlers()
    installCors()
    installRoutes()
}

/**
 * Application routes
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
fun Application.installRoutes() {
    routing {
        metadataEndpoints()
        dataEndpoints()
        indexEndpoints()
        fallbackResponse()
    }
}
