package br.com.dillmann.restdb.domain.metadata.resolver.impl

import br.com.dillmann.restdb.domain.metadata.model.*
import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolver
import br.com.dillmann.restdb.domain.metadata.resolver.utils.column
import br.com.dillmann.restdb.domain.metadata.resolver.utils.columns
import br.com.dillmann.restdb.domain.metadata.resolver.utils.tablePrimaryKeys
import java.sql.Connection

/**
 * [MetadataResolver] implementation for JDBC drivers compatible with catalogs
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-10
 */
object CatalogBasedMetadataResolver : BasicMetadataResolver(), MetadataResolver {

    /**
     * Scans database using JDBC API to load partitions details
     *
     * @param connection JDBC connection
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-03-27
     */
    override fun findPartitions(connection: Connection): Map<String, Partition> {
        return connection.metaData.catalogs
            .use { resultSet ->
                val catalogs = mutableMapOf<String, Partition>()
                while (resultSet.next()) {
                    val name = resultSet.getString("table_cat")
                    catalogs += name to Partition(
                        name = name,
                        tables = findCatalogTables(
                            connection,
                            name
                        )
                    )
                }

                catalogs
            }
    }

    /**
     * Checks if a partition with provided name exists
     *
     * @param connection JDBC connection
     * @param partitionName Partition name
     */
    override fun partitionExists(connection: Connection, partitionName: String): Boolean {
        connection.metaData.catalogs.use { resultSet ->
            while (resultSet.next()) {
                val name = resultSet.getString("table_cat")
                if (partitionName == name) return@partitionExists true
            }
        }

        return false
    }

    /**
     * Checks if a table with provided name exists
     *
     * @param connection JDBC connection
     * @param partitionName Partition name
     * @param tableName Table name
     */
    override fun tableExists(connection: Connection, partitionName: String, tableName: String): Boolean {
        return connection.metaData
            .getTables(partitionName, null, null, arrayOf("TABLE"))
            .use { it.next() }
    }

    /**
     * Scans database using JDBC API to load table details from given [partitionName]
     *
     * @param connection JDBC connection
     * @param partitionName Catalog name
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-03-27
     */
    private fun findCatalogTables(connection: Connection, partitionName: String): Map<String, Table> {
        return connection.metaData.getTables(partitionName, null, null, arrayOf("TABLE"))
            .use { resultSet ->
                val tables = mutableMapOf<String, Table>()
                while (resultSet.next()) {
                    val name = resultSet.getString("table_name")
                    tables += name to Table(
                        name = name,
                        primaryKeyColumns = findTablePrimaryKeyColumns(
                            connection,
                            partitionName,
                            name
                        ),
                        columns = findTableColumns(
                            connection,
                            partitionName,
                            name
                        )
                    )
                }

                tables
            }
    }

    /**
     * Locate the primary key column names for given [tableName]
     *
     * @param connection JDBC connection
     * @param partitionName Catalog name
     * @param tableName Table name
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-03-27
     */
    override fun findTablePrimaryKeyColumns(
        connection: Connection,
        partitionName: String,
        tableName: String
    ): Set<String> {
        return connection.metaData
            .getPrimaryKeys(partitionName, null, tableName)
            .tablePrimaryKeys()
    }

    /**
     * Loads all column details for given [tableName]
     *
     * @param connection JDBC connection
     * @param partitionName Catalog name
     * @param tableName Table name
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-03-27
     */
    override fun findTableColumns(
        connection: Connection,
        partitionName: String,
        tableName: String
    ): Map<String, Column> {
        return connection.metaData
            .getColumns(partitionName, null, tableName, null)
            .columns()
    }

    /**
     * Loads column details for given [columnName]
     *
     * @param connection JDBC connection
     * @param partitionName Partition name
     * @param tableName Table name
     * @param columnName Column name
     */
    override fun findTableColumn(connection: Connection, partitionName: String, tableName: String, columnName: String): Column? {
        return connection
            .metaData
            .getColumns(partitionName, null, tableName, columnName)
            .use {
                if (it.next()) it.column()
                else null
            }
    }

}