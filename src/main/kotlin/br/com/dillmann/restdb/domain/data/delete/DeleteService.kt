package br.com.dillmann.restdb.domain.data.delete

import br.com.dillmann.restdb.core.jdbc.ConnectionPool
import br.com.dillmann.restdb.core.jdbc.runInTransaction
import br.com.dillmann.restdb.domain.data.utils.setParameter
import br.com.dillmann.restdb.domain.data.validateSchemaAndTableName
import br.com.dillmann.restdb.domain.data.validateSinglePrimaryKeyColumn
import br.com.dillmann.restdb.domain.metadata.findTablePrimaryKeyColumns

/**
 * Executes a DELETE operation in database provided [schema], [table] and primary key [rowId] value
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
fun deleteRow(schema: String, table: String, rowId: String) {
    ConnectionPool.startConnection().use { connection ->
        validateSchemaAndTableName(connection, schema, table)
        val primaryKeyColumns = findTablePrimaryKeyColumns(connection, schema, table)
        validateSinglePrimaryKeyColumn(schema, table, primaryKeyColumns)

        val primaryKeyColumn = primaryKeyColumns.first()
        val deleteSqlStatement = "DELETE FROM $schema.$table WHERE $primaryKeyColumn = ?"

        connection.runInTransaction {
            connection.prepareStatement(deleteSqlStatement).use { statement ->
                statement.setParameter(1, rowId)
                statement.execute()
            }
        }
    }

}