package br.com.dillmann.restdb.domain.metadata.openapi.operation

import br.com.dillmann.restdb.domain.metadata.openapi.toJsonFormat
import br.com.dillmann.restdb.domain.metadata.openapi.toJsonType
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.parameters.RequestBody
import java.sql.JDBCType

/**
 * OpenAPI request factory methods for [Operation] objects
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-19
 */
object OperationRequestFactory {

    /**
     * Builds and returns the operation [RequestBody] details
     *
     * @param partitionName Database partition name
     * @param tableName Database table name
     */
    fun buildRequestBody(partitionName: String, tableName: String) =
        RequestBody().also {
            it.required = true
            it.content = Content()

            val mediaType = MediaType()
            mediaType.schema = Schema<Any>()
            mediaType.schema.`$ref` = "#/components/schemas/${partitionName}_${tableName}_row"
            it.content.addMediaType(JSON_MEDIA_TYPE, mediaType)
        }

    /**
     * Builds and returns a path [Parameter] object for the table primary key column
     *
     * @param partitionName Database partition name
     * @param tableName Database table name
     * @param primaryKeyName Table primary key column name
     */
    fun buildPrimaryKeyParameter(partitionName: String, tableName: String, primaryKeyName: String) =
        Parameter().also {
            it.name = primaryKeyName
            it.`in` = "path"
            it.description = "Primary key of table $tableName over partition $partitionName"
            it.allowEmptyValue = false
            it.required = true
            it.schema = Schema<Any>().type("string")
        }

    /**
     * Builds and returns a [List] of query [Parameter] objects for pagination controls
     */
    fun buildPageParameters(): List<Parameter> {
        val page = buildPageParameter(
            "pageNumber",
            "Page number to be returned",
            PAGE_EXAMPLE_VALUE,
            JDBCType.INTEGER
        )
        val pageSize = buildPageParameter(
            "pageSize",
            "How much rows each page should have",
            PAGE_SIZE_EXAMPLE_VALUE,
            JDBCType.INTEGER
        )
        val sorting = buildPageParameter(
            "sort",
            "Sorting instructions",
            "column_a:asc,column_b:desc",
            JDBCType.VARCHAR
        )
        val projection = buildPageParameter(
            "columns",
            "Defines which columns should be returned",
            "column_a,column_b",
            JDBCType.VARCHAR
        )
        val filter = buildPageParameter(
            "filter",
            "Filter instructions",
            "column_a.equals[25]||column_b.isNotNull",
            JDBCType.VARCHAR
        )

        return listOf(page, pageSize, sorting, projection, filter)
    }

    /**
     * Builds and returns a [Parameter] object for a paginated control
     *
     * @param name Page control name
     * @param description Page control description
     * @param example Example value
     */
    private fun buildPageParameter(name: String, description: String, example: Any, type: JDBCType) =
        Parameter().also {
            it.name = name
            it.`in` = "query"
            it.description = description
            it.example = example
            it.required = false
            it.allowEmptyValue = false
            it.schema = Schema<Any>().format(type.toJsonFormat()).type(type.toJsonType())
        }
}