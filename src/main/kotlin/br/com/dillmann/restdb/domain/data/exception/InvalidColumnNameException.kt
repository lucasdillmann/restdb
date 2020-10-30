package br.com.dillmann.restdb.domain.data.exception

import br.com.dillmann.restdb.core.statusPages.exceptions.BadRequestException

/**
 * [BadRequestException] specialization for requests with invalid or unknown column names
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-10-30
 */
class InvalidColumnNameException(partitionName: String, tableName: String, columnName: String) :
    BadRequestException("Column $columnName do not exists in table $tableName of partition $partitionName")