package br.com.dillmann.restdb.domain.data.getRow

import br.com.dillmann.restdb.core.jdbc.ConnectionPool
import br.com.dillmann.restdb.domain.data.utils.retrieveSingleRow
import br.com.dillmann.restdb.domain.data.validateSchemaAndTableName
import br.com.dillmann.restdb.domain.data.validateSinglePrimaryKeyColumn
import br.com.dillmann.restdb.domain.metadata.findTableColumns
import br.com.dillmann.restdb.domain.metadata.findTablePrimaryKeyColumns

/**
 * Selects a single row in provided [schema], [table] and primary key [rowId] value
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
fun findRow(schema: String, table: String, rowId: String): Map<String, Any?>? {
    ConnectionPool.startConnection().use { connection ->
        validateSchemaAndTableName(connection, schema, table)
        val primaryKeyColumns = findTablePrimaryKeyColumns(connection, schema, table)
        validateSinglePrimaryKeyColumn(schema, table, primaryKeyColumns)

        val tableColumns = findTableColumns(connection, schema, table).keys
        val primaryKeyValue = mapOf(primaryKeyColumns.first() to rowId)
        return@findRow retrieveSingleRow(connection, schema, table, primaryKeyColumns, tableColumns, primaryKeyValue)
    }
}