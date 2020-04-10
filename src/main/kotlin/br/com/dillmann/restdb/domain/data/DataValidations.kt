package br.com.dillmann.restdb.domain.data

import br.com.dillmann.restdb.core.statusPages.exceptions.ValidationException
import br.com.dillmann.restdb.core.statusPages.utils.ErrorDetails
import br.com.dillmann.restdb.domain.data.exception.CompositePrimaryKeyException
import br.com.dillmann.restdb.domain.data.exception.InvalidPartitionNameException
import br.com.dillmann.restdb.domain.data.exception.InvalidTableNameException
import br.com.dillmann.restdb.domain.data.exception.NoPrimaryKeyException
import br.com.dillmann.restdb.domain.metadata.model.Column
import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolverFactory
import java.sql.Connection

/**
 * Validates if provided partition and table names exists in database
 *
 * @param connection JDBC connection
 * @param partitionName Partition name
 * @param tableName Table name
 * @throws InvalidPartitionNameException when partition do not exists
 * @throws InvalidTableNameException when table do not exists
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
fun validatePartitionAndTableName(connection: Connection, partitionName: String, tableName: String) {
    val metadataResolver = MetadataResolverFactory.build()
    val partitionExists = metadataResolver.partitionExists(connection, partitionName)
    if (!partitionExists)
        throw InvalidPartitionNameException(partitionName)

    val tableExists = metadataResolver.tableExists(connection, partitionName, tableName)
    if (!tableExists)
        throw InvalidTableNameException(
            partitionName,
            tableName
        )
}

/**
 * Validates user request body, checking if provided column names exists and required values (not null columns) are
 * informed
 *
 * @param receivedColumns User provided column names
 * @param databaseColumns All database column names
 * @param primaryKeyColumns Database primary key columns names
 * @param validateRequiredColumns Flag to check or not if all required values (no null columns) where informed
 * @throws ValidationException when validation detects an inconsistency
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
fun validateRequestBody(
    receivedColumns: Set<String>,
    databaseColumns: Map<String, Column>,
    primaryKeyColumns: Set<String>,
    validateRequiredColumns: Boolean = true
) {

    val unknownColumns = receivedColumns - databaseColumns.keys
    if (unknownColumns.isNotEmpty())
        unknownColumns.throwValidationException("Column do not exists")

    if (validateRequiredColumns) {
        val missingRequiredColumns = databaseColumns.filter { !it.value.nullable }.keys - receivedColumns
        if (missingRequiredColumns.isNotEmpty())
            missingRequiredColumns.throwValidationException("Column do not accept null values")
    }

    val missingPrimaryKeyColumns = primaryKeyColumns - receivedColumns
    if (missingPrimaryKeyColumns.isNotEmpty())
        missingPrimaryKeyColumns.throwValidationException("Primary key column is required")
}

/**
 * Converts an [Set] of [String] column names into a [ValidationException] and throws it
 *
 * @param message Message description to be used in exception [ErrorDetails]
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
private fun Set<String>.throwValidationException(message: String): Nothing {
    val details = map { ErrorDetails(it, message) }
    throw ValidationException(details)
}

/**
 * Checks if table has exactly one primary key column
 *
 * @param partitionName Partition name
 * @param table Table name
 * @param primaryKeyColumns Table primary key columns
 * @throws NoPrimaryKeyException when table do not have primary key columns
 * @throws CompositePrimaryKeyException when table has more than one primary key column
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
fun validateSinglePrimaryKeyColumn(partitionName: String, table: String, primaryKeyColumns: Set<String>) {
    if (primaryKeyColumns.isEmpty())
        throw NoPrimaryKeyException(partitionName, table)

    if (primaryKeyColumns.size > 1)
        throw CompositePrimaryKeyException(
            partitionName,
            table,
            primaryKeyColumns
        )
}