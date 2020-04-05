package br.com.dillmann.restdb.domain.data.post

import br.com.dillmann.restdb.core.jdbc.ConnectionPool
import br.com.dillmann.restdb.core.jdbc.runInTransaction
import br.com.dillmann.restdb.domain.data.utils.retrieveSingleRow
import br.com.dillmann.restdb.domain.data.utils.setParameter
import br.com.dillmann.restdb.domain.data.validateRequestBody
import br.com.dillmann.restdb.domain.data.validateSchemaAndTableName
import br.com.dillmann.restdb.domain.metadata.findTableColumns
import br.com.dillmann.restdb.domain.metadata.findTablePrimaryKeyColumns
import java.sql.Connection

/**
 * Executes a INSERT in database
 *
 * @param data Column names and values to be inserted
 * @param schema Schema name
 * @param table Table name
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
fun createRow(data: Map<String, Any>, schema: String, table: String): Map<String, Any?> {
    ConnectionPool.startConnection().use { connection ->
        validateSchemaAndTableName(connection, schema, table)
        val primaryKeyColumns = findTablePrimaryKeyColumns(connection, schema, table)
        val databaseColumns = findTableColumns(connection, schema, table)
        val receivedColumns = data.keys

        validateRequestBody(receivedColumns, databaseColumns, primaryKeyColumns)
        persistRow(connection, schema, table, receivedColumns, data)
        return@createRow retrieveSingleRow(connection, schema, table, primaryKeyColumns, databaseColumns.keys, data)
            ?: error("Unknown error detected. Created row was not found in database.")
    }
}

/**
 * Persist the creation changes in database
 *
 * @param connection JDBC connection
 * @param schema Schema name
 * @param table Table name
 * @param receivedColumns Names of the columns received in user request
 * @param data Changes to be saved
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
private fun persistRow(
    connection: Connection,
    schema: String,
    table: String,
    receivedColumns: Set<String>,
    data: Map<String, Any>
) {
    val columnNames = receivedColumns.joinToString(separator = ", ")
    val statementVariables = (1..receivedColumns.size).joinToString(separator = ", ") { "?" }
    val insertSqlStatement = "INSERT INTO $schema.$table ($columnNames) VALUES ($statementVariables)"

    connection.runInTransaction {
        connection.prepareStatement(insertSqlStatement).use { statement ->
            data.entries.forEachIndexed { index, (_, value) ->
                statement.setParameter(index + 1, value)
            }
            statement.execute()
        }
    }
}
