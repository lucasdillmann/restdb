package br.com.dillmann.restdb.core.statusPages.exceptions

import br.com.dillmann.restdb.core.statusPages.utils.ErrorDetails
import io.ktor.http.HttpStatusCode

/**
 * Specialization of [HttpException] for request validation violations
 *
 * @param details Violation details
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
class ValidationException(val details: List<ErrorDetails>) :
    HttpException(
        "Your request cannot be accepted. Check the problems found and try again.",
        HttpStatusCode.UnprocessableEntity
    )