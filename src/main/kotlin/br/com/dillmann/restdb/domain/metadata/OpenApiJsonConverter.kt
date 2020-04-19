package br.com.dillmann.restdb.domain.metadata

import br.com.dillmann.restdb.core.configureDefaults
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.models.OpenAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * [OpenAPI] to JSON converter with support for Kotlin Coroutines
 *
 * This class is able to convert an [OpenAPI] input to a [String] JSON using [ObjectMapper] without
 * blocking the thread
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-19
 */
object OpenApiJsonConverter {

    private val mapper by lazy { buildObjectMapper() }

    /**
     * Converts the [OpenAPI] input to a [String] JSON
     */
    suspend fun convert(input: OpenAPI): String {
        return withContext(Dispatchers.IO) {
            mapper.writeValueAsString(input)
        }
    }

    /**
     * Builds and returns a customized [ObjectMapper] suitable for [OpenAPI] objects
     */
    private fun buildObjectMapper(): ObjectMapper =
        ObjectMapper()
            .configureDefaults()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)

}