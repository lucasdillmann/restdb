package br.com.dillmann.restdb.core.statusPages.exceptions

import io.ktor.http.HttpStatusCode

/**
 * Resource not found specialization of HTTP exception
 *
 * @param partitionName Database partition name
 * @param tableName Database table name
 * @param primaryKeyValue Table primary key column value
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-26
 */
class NotFoundException(partitionName: String, tableName: String, primaryKeyValue: String) :
    HttpException(
        "No row found over table $tableName on partition $partitionName with primary key value '$primaryKeyValue'",
        HttpStatusCode.NotFound
    )