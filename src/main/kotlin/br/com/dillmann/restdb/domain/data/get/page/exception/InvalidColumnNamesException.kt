package br.com.dillmann.restdb.domain.data.get.page.exception

import br.com.dillmann.restdb.core.statusPages.exceptions.BadRequestException

/**
 * [BadRequestException] specialization for user provided invalid database column names
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
class InvalidColumnNamesException(columnNames: Set<String>) :
    BadRequestException("Invalid or nonexistent column name(s): ${columnNames.joinToString()}.")