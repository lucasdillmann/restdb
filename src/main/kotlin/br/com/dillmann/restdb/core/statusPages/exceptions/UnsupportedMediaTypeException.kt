package br.com.dillmann.restdb.core.statusPages.exceptions

import io.ktor.http.HttpStatusCode

/**
 * Specialization of [HttpException] for requests with an unknown or unsupported content type
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
class UnsupportedMediaTypeException :
    HttpException("Request media type is not supported. Retry with a JSON body.", HttpStatusCode.UnsupportedMediaType)