package br.com.dillmann.restdb.domain.data.utils

import br.com.dillmann.restdb.core.statusPages.exceptions.MissingBodyException
import br.com.dillmann.restdb.core.statusPages.exceptions.UnsupportedMediaTypeException
import br.com.dillmann.restdb.domain.data.exception.InvalidQueryParameterValueException
import br.com.dillmann.restdb.domain.data.exception.MissingUriParameterException
import io.ktor.application.ApplicationCall
import io.ktor.features.ContentTransformationException
import io.ktor.request.receive
import io.ktor.features.UnsupportedMediaTypeException as KtorUnsupportedMediaTypeException

/**
 * Reads request body as a JSON from user request
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
suspend fun ApplicationCall.jsonBody(): Map<String, Any> {
    try {
        return receive<Map<String, Any>>()
            .takeIf { it.isNotEmpty() }
            ?: throw MissingBodyException()
    } catch (ex: KtorUnsupportedMediaTypeException) {
        throw UnsupportedMediaTypeException()
    } catch (ex: ContentTransformationException) {
        throw MissingBodyException()
    }
}

/**
 * Reads a query parameter from user request
 *
 * @param parameterName Query parameter name
 * @param defaultValue Default value to be used when user did not provided a value (a null is found)
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
fun ApplicationCall.getQueryParameter(parameterName: String, defaultValue: String) =
    getQueryParameter(parameterName, defaultValue) { it }

/**
 * Reads a query parameter from user request
 *
 * @param parameterName Query parameter name
 * @param defaultValue Default value to be used when user did not provided a value (a null is found)
 * @param converter Value converter from [String] to [T]
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
fun <T> ApplicationCall.getQueryParameter(parameterName: String, defaultValue: T, converter: (String) -> T): T {
    val parameterValue = request.queryParameters[parameterName] ?: return defaultValue

    try {
        return converter(parameterValue)
    } catch (ex: Exception) {
        throw InvalidQueryParameterValueException(
            parameterName,
            parameterValue
        )
    }
}

/**
 * Reads schema and table names from route variables, throwing [MissingUriParameterException] when they are not
 * found
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
fun ApplicationCall.mainRequestParameters(): Pair<String, String> {
    val schemaName = parameters["schema"] ?: throw MissingUriParameterException(
        "schema"
    )
    val tableName = parameters["table"] ?: throw MissingUriParameterException(
        "table"
    )
    return schemaName to tableName
}

/**
 * Reads row ID from route variables, throwing [MissingUriParameterException] if it is not found
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
fun ApplicationCall.rowId(): String =
    parameters["id"] ?: throw MissingUriParameterException(
        "id"
    )