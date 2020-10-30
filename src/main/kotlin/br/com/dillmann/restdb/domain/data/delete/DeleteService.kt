package br.com.dillmann.restdb.domain.data.delete

import br.com.dillmann.restdb.core.jdbc.ConnectionPool
import br.com.dillmann.restdb.core.jdbc.runInTransaction
import br.com.dillmann.restdb.domain.data.utils.setParameter
import br.com.dillmann.restdb.domain.data.validatePartitionAndTableName
import br.com.dillmann.restdb.domain.data.validateSinglePrimaryKeyColumn
import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolverFactory

/**
 * Executes a DELETE operation in database provided [partition], [table] and primary key [rowId] value
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
fun deleteRow(partition: String, table: String, rowId: String) {
    ConnectionPool.startConnection().use { connection ->
        validatePartitionAndTableName(connection, partition, table)
        val primaryKeyColumns = MetadataResolverFactory.build().findTablePrimaryKeyColumns(connection, partition, table)
        validateSinglePrimaryKeyColumn(partition, table, primaryKeyColumns)

        val primaryKeyColumn = primaryKeyColumns.first()
        val deleteSqlStatement = "DELETE FROM $partition.$table WHERE $primaryKeyColumn = ?"

        connection.runInTransaction {
            connection.prepareStatement(deleteSqlStatement).use { statement ->
                statement.setParameter(1, rowId, partition, table, primaryKeyColumn)
                statement.execute()
            }
        }
    }

}