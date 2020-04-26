package br.com.dillmann.restdb.domain.metadata.resolver.impl

import br.com.dillmann.restdb.domain.metadata.model.*
import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolver
import br.com.dillmann.restdb.domain.metadata.resolver.utils.columns
import br.com.dillmann.restdb.domain.metadata.resolver.utils.tablePrimaryKeys
import java.sql.Connection

/**
 * [MetadataResolver] implementation for JDBC drivers compatible with schemas
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-10
 */
object SchemaBasedMetadataResolver : BasicMetadataResolver(), MetadataResolver {

    /**
     * Scans database using JDBC API to load partitions details
     *
     * @param connection JDBC connection
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-03-27
     */
    override fun findPartitions(connection: Connection): Map<String, Partition> {
        return connection.metaData.schemas
            .use { resultSet ->
                val schemas = mutableMapOf<String, Partition>()
                while (resultSet.next()) {
                    val name =
                        resultSet.getString("table_schem") // it is not a typo, jdbc uses "table_schem" as result column name
                    schemas += name to Partition(
                        name = name,
                        tables = findSchemaTables(
                            connection,
                            name
                        )
                    )
                }

                schemas
            }
    }

    /**
     * Checks if a partition with provided name exists
     *
     * @param connection JDBC connection
     * @param partitionName Partition name
     */
    override fun partitionExists(connection: Connection, partitionName: String): Boolean {
        connection.metaData.schemas.use { resultSet ->
            while (resultSet.next()) {
                val name = resultSet.getString("table_schem")
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
            .getTables(null, partitionName, tableName, arrayOf("TABLE"))
            .use { it.next() }
    }

    /**
     * Scans database using JDBC API to load table details from given [partitionName]
     *
     * @param connection JDBC connection
     * @param partitionName Partition name
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-03-27
     */
    private fun findSchemaTables(connection: Connection, partitionName: String): Map<String, Table> {
        return connection.metaData.getTables(null, partitionName, null, arrayOf("TABLE"))
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
     * @param partitionName Partition name
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
            .getPrimaryKeys(null, partitionName, tableName)
            .tablePrimaryKeys()
    }

    /**
     * Loads all column details for given [tableName]
     *
     * @param connection JDBC connection
     * @param partitionName Partition name
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
            .getColumns(null, partitionName, tableName, null)
            .columns()
    }

}