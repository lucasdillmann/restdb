package br.com.dillmann.restdb.domain.data.exception

import br.com.dillmann.restdb.core.statusPages.exceptions.BadRequestException

/**
 * [BadRequestException] specialization for requests with invalid or unknown schema names
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
class InvalidSchemaNameException(schemaName: String) :
    BadRequestException("Schema $schemaName do not exists")