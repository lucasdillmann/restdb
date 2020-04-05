package br.com.dillmann.restdb.domain.data.exception

import br.com.dillmann.restdb.core.statusPages.exceptions.BadRequestException

/**
 * [BadRequestException] specialization for requests with invalid or missing query parameters
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
class InvalidQueryParameterValueException(parameterName: String, parameterValue: String?) :
    BadRequestException("Query parameter $parameterName has an invalid value: $parameterValue")