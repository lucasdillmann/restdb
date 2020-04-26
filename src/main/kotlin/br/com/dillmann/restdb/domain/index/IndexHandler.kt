package br.com.dillmann.restdb.domain.index

import br.com.dillmann.restdb.core.environment.EnvironmentVariables
import br.com.dillmann.restdb.core.jdbc.ConnectionPool
import br.com.dillmann.restdb.domain.metadata.model.Table
import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolverFactory
import io.ktor.application.ApplicationCall
import io.ktor.response.respond

/**
 * Handles GET requests for application index
 *
 * @param call Application call
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-25
 */
suspend fun handleGetIndex(call: ApplicationCall) {
    val openApiLink = link("openapi", "/metadata/openapi.json")
    val metadataLink = link("metadata", "/metadata")

    val databaseLinks = ConnectionPool
        .startConnection()
        .use(MetadataResolverFactory.build()::findDatabaseMetadata)
        .partitions
        .flatMap { (name, partition) -> partition.tables.values.map { buildLinks(name, it) } }
        .takeIf { it.isNotEmpty() }
        ?.reduce { accumulator, list -> accumulator + list }
        ?: emptyList()

    val hateoasJsonContent = mapOf(
        "_links" to listOf(openApiLink, metadataLink) + databaseLinks
    )

    call.respond(hateoasJsonContent)
}

/**
 * Creates a [Link] with target prefixed with server host and port from [EnvironmentVariables]
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-25
 */
private fun link(rel: String, target: String): Link {
    val externalUrl = EnvironmentVariables.externalUrl
    return Link(rel, "$externalUrl$target")
}

/**
 * Builds a [List] of [Link] pointing to table list endpoint and, when compatible, get row by primary key endpoint
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-25
 */
private fun buildLinks(partition: String, tableDetails: Table): List<Link> {
    val tableName = tableDetails.name
    val result = mutableListOf(
        link(
            "$partition-$tableName-list",
            "/data/$partition/$tableName{?pageNumber,pageSize,sort,columns,filter}"
        )
    )
    if (tableDetails.primaryKeyColumns.size == 1) {
        val primaryKeyName = tableDetails.primaryKeyColumns.first()
        result += link(
            "$partition-$tableName-row",
            "/data/$partition/$tableName/{$primaryKeyName}"
        )
    }

    return result
}