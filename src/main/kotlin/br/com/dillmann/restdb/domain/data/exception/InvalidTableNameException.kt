package br.com.dillmann.restdb.domain.data.exception

import br.com.dillmann.restdb.core.statusPages.exceptions.BadRequestException

/**
 * [BadRequestException] specialization for requests with invalid or unknown table names
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
class InvalidTableNameException(partitionName: String, tableName: String) :
    BadRequestException("Table $tableName do not exists in partition $partitionName")