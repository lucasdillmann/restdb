package br.com.dillmann.restdb.domain.data.put

import br.com.dillmann.restdb.core.jdbc.ConnectionPool
import br.com.dillmann.restdb.core.jdbc.runInTransaction
import br.com.dillmann.restdb.domain.data.utils.retrieveSingleRow
import br.com.dillmann.restdb.domain.data.utils.setParameter
import br.com.dillmann.restdb.domain.data.validateRequestBody
import br.com.dillmann.restdb.domain.data.validateSchemaAndTableName
import br.com.dillmann.restdb.domain.data.validateSinglePrimaryKeyColumn
import br.com.dillmann.restdb.domain.metadata.Column
import br.com.dillmann.restdb.domain.metadata.findTableColumns
import br.com.dillmann.restdb.domain.metadata.findTablePrimaryKeyColumns
import java.sql.Connection

/**
 * Executes a UPDATE in database using PUT strategy
 *
 * @param data Column names and values to be updated
 * @param schema Schema name
 * @param table Table name
 * @param rowId Primary key column value
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
fun updateRow(data: Map<String, Any>, schema: String, table: String, rowId: String): Map<String, Any?> {
    ConnectionPool.startConnection().use { connection ->
        validateSchemaAndTableName(connection, schema, table)

        val primaryKeyColumns = findTablePrimaryKeyColumns(connection, schema, table)
        validateSinglePrimaryKeyColumn(schema, table, primaryKeyColumns)

        val databaseColumns = findTableColumns(connection, schema, table)
        val receivedColumns = data.keys

        validateRequestBody(receivedColumns, databaseColumns, primaryKeyColumns)
        persistRow(connection, schema, table, databaseColumns, primaryKeyColumns.first(), rowId, data)
        return@updateRow retrieveSingleRow(
            connection,
            schema,
            table,
            primaryKeyColumns,
            databaseColumns.keys,
            mapOf(primaryKeyColumns.first() to rowId)
        ) ?: error("Unknown error detected. Updated row was not found in database.")
    }
}

/**
 * Persist the row changes in database
 *
 * @param connection JDBC connection
 * @param schema Schema name
 * @param table Table name
 * @param databaseColumns All database columns details
 * @param primaryKeyColumn Primary key column name
 * @param rowId Primary key column value
 * @param data Changes to be saved
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
private fun persistRow(
    connection: Connection,
    schema: String,
    table: String,
    databaseColumns: Map<String, Column>,
    primaryKeyColumn: String,
    rowId: String,
    data: Map<String, Any>
) {
    val columnNames = databaseColumns.keys.joinToString(separator = ", ") { "$it = ?" }
    val updateSqlStatement = "UPDATE $schema.$table SET $columnNames WHERE $primaryKeyColumn = ?"

    connection.runInTransaction {
        connection.prepareStatement(updateSqlStatement).use { statement ->
            var index = 1
            databaseColumns.keys.forEach { statement.setParameter(index++, data[it]) }
            statement.setParameter(index, rowId)
            statement.execute()
        }
    }
}




