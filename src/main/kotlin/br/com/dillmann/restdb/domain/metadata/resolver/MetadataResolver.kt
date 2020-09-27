package br.com.dillmann.restdb.domain.metadata.resolver

import br.com.dillmann.restdb.domain.metadata.model.Column
import br.com.dillmann.restdb.domain.metadata.model.DatabaseMetadata
import br.com.dillmann.restdb.domain.metadata.model.Partition
import br.com.dillmann.restdb.domain.metadata.model.ProductsDetails
import java.sql.Connection

/**
 * JDBC metadata resolver contract
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-10
 */
interface MetadataResolver {

    /**
     * Scans database using JDBC API to load partitions and tables metadata
     *
     * @param connection JDBC connection
     */
    fun findDatabaseMetadata(connection: Connection): DatabaseMetadata

    /**
     * Locates database and JDBC driver product details
     *
     * @param connection JDBC connection
     */
    fun findProductDetails(connection: Connection): ProductsDetails

    /**
     * Scans database using JDBC API to load partitions details
     *
     * @param connection JDBC connection
     */
    fun findPartitions(connection: Connection): Map<String, Partition>

    /**
     * Checks if a partition with provided name exists
     *
     * @param connection JDBC connection
     * @param partitionName Partition name
     */
    fun partitionExists(connection: Connection, partitionName: String): Boolean

    /**
     * Checks if a table with provided name exists
     *
     * @param connection JDBC connection
     * @param partitionName Partition name
     * @param tableName Table name
     */
    fun tableExists(connection: Connection, partitionName: String, tableName: String): Boolean

    /**
     * Locate the primary key column names for given [tableName]
     *
     * @param connection JDBC connection
     * @param partitionName Partition name
     * @param tableName Table name
     */
    fun findTablePrimaryKeyColumns(connection: Connection, partitionName: String, tableName: String): Set<String>

    /**
     * Loads all column details for given [tableName]
     *
     * @param connection JDBC connection
     * @param partitionName Partition name
     * @param tableName Table name
     */
    fun findTableColumns(connection: Connection, partitionName: String, tableName: String): Map<String, Column>

    /**
     * Loads column details for given [columnName]
     *
     * @param connection JDBC connection
     * @param partitionName Partition name
     * @param tableName Table name
     * @param columnName Column name
     */
    fun findTableColumn(connection: Connection, partitionName: String, tableName: String, columnName: String): Column?
}