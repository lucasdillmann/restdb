package br.com.dillmann.restdb.domain.data.post

import br.com.dillmann.restdb.core.jdbc.ConnectionPool
import br.com.dillmann.restdb.core.jdbc.runInTransaction
import br.com.dillmann.restdb.domain.data.utils.retrieveSingleRow
import br.com.dillmann.restdb.domain.data.utils.setParameter
import br.com.dillmann.restdb.domain.data.validatePartitionAndTableName
import br.com.dillmann.restdb.domain.data.validateRequestBody
import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolverFactory
import java.sql.Connection

/**
 * Executes a INSERT in database
 *
 * @param data Column names and values to be inserted
 * @param partition Partition name
 * @param table Table name
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
fun createRow(data: Map<String, Any>, partition: String, table: String): Map<String, Any?> {
    return ConnectionPool.startConnection().use { connection ->
        validatePartitionAndTableName(connection, partition, table)
        val metadataResolver = MetadataResolverFactory.build()
        val primaryKeyColumns = metadataResolver.findTablePrimaryKeyColumns(connection, partition, table)
        val databaseColumns = metadataResolver.findTableColumns(connection, partition, table)
        val receivedColumns = data.keys

        validateRequestBody(receivedColumns, databaseColumns, primaryKeyColumns)
        persistRow(connection, partition, table, receivedColumns, data)
        retrieveSingleRow(connection, partition, table, primaryKeyColumns, databaseColumns.keys, data)
            ?: error("Unknown error detected. Created row was not found in database.")
    }
}

/**
 * Persist the creation changes in database
 *
 * @param connection JDBC connection
 * @param partition Partition name
 * @param table Table name
 * @param receivedColumns Names of the columns received in user request
 * @param data Changes to be saved
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
private fun persistRow(
    connection: Connection,
    partition: String,
    table: String,
    receivedColumns: Set<String>,
    data: Map<String, Any>
) {
    val columnNames = receivedColumns.joinToString(separator = ", ")
    val statementVariables = (1..receivedColumns.size).joinToString(separator = ", ") { "?" }
    val insertSqlStatement = "INSERT INTO $partition.$table ($columnNames) VALUES ($statementVariables)"

    connection.runInTransaction {
        connection.prepareStatement(insertSqlStatement).use { statement ->
            data.entries.forEachIndexed { index, (_, value) ->
                statement.setParameter(index + 1, value)
            }
            statement.execute()
        }
    }
}

