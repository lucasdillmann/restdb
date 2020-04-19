package br.com.dillmann.restdb.domain.metadata.openapi

import br.com.dillmann.restdb.domain.metadata.model.DatabaseMetadata
import br.com.dillmann.restdb.domain.metadata.model.Table
import br.com.dillmann.restdb.domain.metadata.openapi.operation.OperationBuilder
import io.ktor.http.HttpMethod
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.parameters.Parameter

/**
 * Converts a [DatabaseMetadata] into an OpenAPI [Paths] object
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
fun DatabaseMetadata.asOpenApiPaths(): Paths {
    val pathItems = partitions.values
        .flatMap { it.tables.values.map { table -> it.name to table } }
        .map { (partitionName, table) -> buildPaths(partitionName, table) }
        .takeIf { it.isNotEmpty() }
        ?.reduce { accumulator, map -> accumulator + map }
        ?: emptyMap()

    return Paths().also { it.putAll(pathItems) }
}

/**
 * Builds and returns a [Map] of all [Paths] of the given [partitionName] and [table]. Each element
 * in the [Map] represents a OpenAPI [Operation] supported by the input.
 *
 * @param partitionName Database partition name
 * @param table Database table details
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
private fun buildPaths(partitionName: String, table: Table): Map<String, PathItem> {
    val result = mutableMapOf<String, PathItem>()
    val tableName = table.name
    val tablePrimaryKeys = table.primaryKeyColumns

    if (tablePrimaryKeys.size == 1)
        result += buildPathsById(partitionName, tableName, tablePrimaryKeys.first())

    val operation: (HttpMethod, Boolean) -> OperationBuilder = { method, paged ->
        OperationBuilder(
            partitionName,
            tableName,
            method
        ).also { if (paged) it.pagedContract() }
    }
    val pathItem = PathItem().also {
        it.description = "Paginated query results for table $tableName under partition $partitionName"
        it.get = operation(HttpMethod.Get, true).build()
        it.post = operation(HttpMethod.Post, false).build()
    }

    val url = "/data/$partitionName/$tableName"
    result += url to pathItem
    return result
}

/**
 * Builds and returns a [PathItem] for the provided [partitionName], [tableName] and [primaryKeyName]. Each element
 * in the result represents a OpenAPI [Operation] supported by the input. This method is only suitable for
 * tables with exactly one primary key column.
 *
 * @param partitionName Database partition name
 * @param tableName Database table name
 * @param primaryKeyName Primary key column name
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
private fun buildPathsById(partitionName: String, tableName: String, primaryKeyName: String): Pair<String, PathItem> {
    val url = "/data/$partitionName/$tableName/{$primaryKeyName}"
    val parameter = Parameter().also {
        it.name = primaryKeyName
        it.description = "Primary key value for table $tableName under partition $partitionName"
        it.required = true
        it.allowEmptyValue = false
    }

    val operation: (HttpMethod) -> OperationBuilder = {
        OperationBuilder(
            partitionName,
            tableName,
            it
        ).singleRow(primaryKeyName)
    }
    val pathItem = PathItem().also {
        it.description = "Single row manipulation over table $tableName under partition $partitionName"
        it.parameters = listOf(parameter)
        it.get = operation(HttpMethod.Get).build()
        it.put = operation(HttpMethod.Put).build()
        it.patch = operation(HttpMethod.Patch).build()
        it.delete = operation(HttpMethod.Delete).build()
    }

    return url to pathItem
}

