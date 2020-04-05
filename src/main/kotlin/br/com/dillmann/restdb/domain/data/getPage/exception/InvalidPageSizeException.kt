package br.com.dillmann.restdb.domain.data.getPage.exception

import br.com.dillmann.restdb.core.statusPages.exceptions.BadRequestException

/**
 * [BadRequestException] specialization for user provided invalid page sizes
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
class InvalidPageSizeException(pageSize: Long) :
    BadRequestException("Invalid page size $pageSize. Retry with a value equal to or bigger than 1.")