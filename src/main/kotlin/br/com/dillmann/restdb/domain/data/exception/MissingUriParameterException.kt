package br.com.dillmann.restdb.domain.data.exception

import br.com.dillmann.restdb.core.statusPages.exceptions.BadRequestException

/**
 * [BadRequestException] specialization for requests with missing uri/route parameters
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
class MissingUriParameterException(parameterName: String) :
    BadRequestException("URI parameter $parameterName is required")