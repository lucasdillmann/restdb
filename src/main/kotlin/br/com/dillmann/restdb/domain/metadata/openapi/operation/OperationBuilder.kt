package br.com.dillmann.restdb.domain.metadata.openapi.operation

import io.ktor.http.HttpMethod
import io.swagger.v3.oas.models.Operation

/**
 * Builder class for OpenAPI [Operation] objects
 *
 * @param partition Database partition name of the operation
 * @param table Database table name of the operations
 * @param method HTTP method of the operation
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
class OperationBuilder(
    private val partition: String,
    private val table: String,
    private val method: HttpMethod
) {
    private val methodsWithRequestBody = listOf(HttpMethod.Post, HttpMethod.Put, HttpMethod.Patch)
    private val modificationMethods = listOf(HttpMethod.Post, HttpMethod.Put, HttpMethod.Patch, HttpMethod.Delete)
    private var paged = false
    private var primaryKeyName: String? = null

    /**
     * Ends the build flow and returns the result [Operation] object
     */
    fun build(): Operation =
        Operation().also {
            if (method in methodsWithRequestBody)
                it.requestBody = OperationRequestFactory.buildRequestBody(partition, table)

            it.responses = when {
                method in modificationMethods -> OperationResponseFactory.buildModificationResponses(partition, table)
                paged -> OperationResponseFactory.buildPagedResponses(partition, table)
                else -> OperationResponseFactory.buildBasicSingleRowResponses(partition, table)
            }

            it.parameters = when {
                primaryKeyName != null -> listOf(
                    OperationRequestFactory.buildPrimaryKeyParameter(partition, table, primaryKeyName!!)
                )
                paged -> OperationRequestFactory.buildPageParameters()
                else -> null
            }
        }

    /**
     * Signals that current object uses a paginated result contract
     */
    fun pagedContract(): OperationBuilder {
        this.paged = true
        this.primaryKeyName = null
        return this
    }

    /**
     * Signals that current objects uses a single row contract
     */
    fun singleRow(primaryKeyName: String): OperationBuilder {
        this.primaryKeyName = primaryKeyName
        this.paged = false
        return this
    }

}
