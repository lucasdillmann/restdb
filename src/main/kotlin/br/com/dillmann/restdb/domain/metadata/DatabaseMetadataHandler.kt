package br.com.dillmann.restdb.domain.metadata

import br.com.dillmann.restdb.core.jdbc.ConnectionPool
import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolverFactory
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
    val metadataResolver = MetadataResolverFactory.build()
    val metadata = ConnectionPool.startConnection().use(metadataResolver::findDatabaseMetadata)
    call.respond(metadata)
}