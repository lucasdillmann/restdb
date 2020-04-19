package br.com.dillmann.restdb.domain.metadata

import br.com.dillmann.restdb.core.jdbc.ConnectionPool
import br.com.dillmann.restdb.domain.metadata.model.DatabaseMetadata
import br.com.dillmann.restdb.domain.metadata.openapi.asOpenApi
import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolverFactory
import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText

/**
 * Handles GET requests for database metadata
 *
 * @param call Application call
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-01
 */
suspend fun handleGetMetadata(call: ApplicationCall) {
    val metadata = getMetadata()
    call.respond(metadata)
}

/**
 * Handles GET requests for database metadata in OpenAPI format
 *
 * @param call Application call
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-10
 */
suspend fun handleGetOpenApi(call: ApplicationCall) {
    val openApi = getMetadata().asOpenApi()

    // standard application converter is not used to not serialize null values
    val json = OpenApiJsonConverter.convert(openApi)
    call.respondText(contentType = ContentType.Application.Json, text = json)
}

/**
 * Retrieves database metadata
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
private fun getMetadata(): DatabaseMetadata {
    val metadataResolver = MetadataResolverFactory.build()
    return ConnectionPool.startConnection().use(metadataResolver::findDatabaseMetadata)
}