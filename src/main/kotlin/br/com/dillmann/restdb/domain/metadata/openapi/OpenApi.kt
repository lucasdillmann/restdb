package br.com.dillmann.restdb.domain.metadata.openapi

import br.com.dillmann.restdb.core.environment.EnvironmentVariables
import br.com.dillmann.restdb.domain.metadata.model.DatabaseMetadata
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server

/**
 * Converts a [DatabaseMetadata] into an [OpenAPI] object
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
fun DatabaseMetadata.asOpenApi(): OpenAPI =
    OpenAPI().also {
        it.info = asOpenApiInfo()
        it.externalDocs = buildExternalDocumentation()
        it.servers = buildServers()
        it.paths = asOpenApiPaths()
        it.components = asOpenApiComponents()
    }

/**
 * Converts a [DatabaseMetadata] into an OpenAPI [Components] object
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
private fun DatabaseMetadata.asOpenApiComponents() =
    Components().also {
        it.schemas = asOpenApiSchemas()
    }

/**
 * Converts a [DatabaseMetadata] into an OpenAPI [Info] object
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
private fun DatabaseMetadata.asOpenApiInfo(): Info =
    Info().also {
        it.title = "restdb"
        it.description = "HTTP REST APIs for $database using $driver"
        it.version = EnvironmentVariables.applicationVersion
    }

/**
 * Creates an [ExternalDocumentation] with reference to project GitHub repository
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
private fun buildExternalDocumentation(): ExternalDocumentation =
    ExternalDocumentation().also {
        it.description = "restdb documentation"
        it.url = "https://github.com/lucasdillmann/restdb"
    }

/**
 * Creates a [List] of OpenAPI [Server] objects with reference to current application host and port
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
private fun buildServers(): List<Server> {
    val server = Server().also {
        it.url = EnvironmentVariables.externalUrl
    }

    return listOf(server)
}
