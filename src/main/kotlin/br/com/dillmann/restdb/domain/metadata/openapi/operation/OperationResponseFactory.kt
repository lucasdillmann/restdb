package br.com.dillmann.restdb.domain.metadata.openapi.operation

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses

/**
 * OpenAPI responses factory methods for [Operation] objects
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-19
 */
object OperationResponseFactory {

    /**
     * Builds and returns an [ApiResponses] for operations with single row contract (like GET)
     *
     * @param partitionName Database partition name
     * @param tableName Database table name
     */
    fun buildBasicSingleRowResponses(partitionName: String, tableName: String): ApiResponses {
        val notFound = buildErrorResponse("Resource not found")
        val result = buildBasicResponses(partitionName, tableName)
        result += "404" to notFound
        return result
    }

    /**
     * Builds and returns an [ApiResponses] for modification operations (like PUT, POST and PATCH)
     *
     * @param partitionName Database partition name
     * @param tableName Database table name
     */
    fun buildModificationResponses(partitionName: String, tableName: String): ApiResponses {
        val unprocessableEntity = buildErrorResponse("Operation unsuccessful due to validation violations")
        val badRequest = buildErrorResponse("Operation unsuccessful due to invalid request")

        val result = buildBasicSingleRowResponses(partitionName, tableName)
        result += "400" to badRequest
        result += "422" to unprocessableEntity
        return result
    }

    /**
     * Builds and returns an [ApiResponses] for operations with paginated contract (like GET)
     *
     * @param partitionName Database partition name
     * @param tableName Database table name
     */
    fun buildPagedResponses(partitionName: String, tableName: String): ApiResponses {
        val response = buildBasicResponses(partitionName, tableName)
        response["200"]!!.content[JSON_MEDIA_TYPE]!!.schema.`$ref` =
            "#/components/schemas/${partitionName}_${tableName}_page"
        return response
    }

    /**
     * Builds a [JSON_MEDIA_TYPE] [MediaType] object for the provided reference
     *
     * @param reference OpenAPI Schema reference
     */
    private fun buildContentType(reference: String): Content {
        val mediaType = MediaType().also {
            it.schema = Schema<Any>()
            it.schema.`$ref` = "#/components/schemas/$reference"
        }

        return Content()
            .addMediaType(JSON_MEDIA_TYPE, mediaType)
    }

    /**
     * Builds and returns an [ApiResponses] used by all operations (GET, POST, PUT, etc)
     *
     * @param partitionName Database partition name
     * @param tableName Database table name
     */
    private fun buildBasicResponses(partitionName: String, tableName: String): ApiResponses {
        val ok = ApiResponse().also {
            it.description = "Operation completed with success"
            it.content = buildContentType("${partitionName}_${tableName}_row")
        }

        val result = ApiResponses()
        result += "200" to ok
        return result
    }

    /**
     * Builds and returns a [ApiResponse] using an error response [JSON_MEDIA_TYPE] [MediaType]
     */
    private fun buildErrorResponse(description: String) =
        ApiResponse().also {
            it.description = description
            it.content = Content()

            val mediaType = MediaType()
            mediaType.schema = Schema<Any>()
            mediaType.schema.`$ref` = "#/components/schemas/error_details"
            it.content.addMediaType(JSON_MEDIA_TYPE, mediaType)
        }
}