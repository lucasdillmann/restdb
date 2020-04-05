package br.com.dillmann.restdb.core.statusPages.exceptions

import io.ktor.http.HttpStatusCode

/**
 * Bad request specialization of HTTP exception
 *
 * @param message Message to be returned to user
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
abstract class BadRequestException(message: String) :
    HttpException(message, HttpStatusCode.BadRequest)