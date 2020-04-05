package br.com.dillmann.restdb.core.statusPages.exceptions

import io.ktor.http.HttpStatusCode

/**
 * Method not allowed specialization of HTTP exception
 *
 * @param requestMethod HTTP method of the current request
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
class MethodNotAllowedException(requestMethod: String) :
    HttpException("Request method $requestMethod is not allowed", HttpStatusCode.MethodNotAllowed)