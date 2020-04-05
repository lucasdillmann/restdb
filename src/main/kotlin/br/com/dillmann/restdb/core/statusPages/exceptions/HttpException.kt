package br.com.dillmann.restdb.core.statusPages.exceptions

import io.ktor.http.HttpStatusCode

/**
 * HTTP exception definition
 *
 * @param message Message to be returned to user
 * @param statusCode HTTP status code to be used in response
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
abstract class HttpException(message: String, val statusCode: HttpStatusCode) :
    RuntimeException(message)
