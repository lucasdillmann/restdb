package br.com.dillmann.restdb.domain.data.get.singleRow

import br.com.dillmann.restdb.core.jdbc.ConnectionPool
import br.com.dillmann.restdb.domain.data.utils.retrieveSingleRow
import br.com.dillmann.restdb.domain.data.validatePartitionAndTableName
import br.com.dillmann.restdb.domain.data.validateSinglePrimaryKeyColumn
import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolverFactory

/**
 * Selects a single row in provided [partition], [table] and primary key [rowId] value
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
fun findRow(partition: String, table: String, rowId: String): Map<String, Any?>? {
    return ConnectionPool.startConnection().use { connection ->
        validatePartitionAndTableName(connection, partition, table)
        val metadataResolver = MetadataResolverFactory.build()
        val primaryKeyColumns = metadataResolver.findTablePrimaryKeyColumns(connection, partition, table)
        validateSinglePrimaryKeyColumn(partition, table, primaryKeyColumns)

        val tableColumns = metadataResolver.findTableColumns(connection, partition, table).keys
        val primaryKeyValue = mapOf(primaryKeyColumns.first() to rowId)
        retrieveSingleRow(connection, partition, table, primaryKeyColumns, tableColumns, primaryKeyValue)
    }
}