package br.com.dillmann.restdb.domain.metadata.cache

import br.com.dillmann.restdb.core.environment.EnvironmentVariables
import br.com.dillmann.restdb.core.jdbc.ConnectionPool
import br.com.dillmann.restdb.domain.metadata.model.Column
import br.com.dillmann.restdb.domain.metadata.model.DatabaseMetadata
import br.com.dillmann.restdb.domain.metadata.model.Partition
import br.com.dillmann.restdb.domain.metadata.model.ProductsDetails
import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolver
import org.slf4j.LoggerFactory
import java.sql.Connection

/**
 * [MetadataCache] proxy for [MetadataResolver]
 *
 * This class acts as a proxy between the database metadata resolution API and the in-memory cache, allowing
 * fast metadata resolution after application was started.
 *
 * Cache behaviour is enabled by default but can be disabled by environment variables. See
 * [EnvironmentVariables.cacheDatabaseMetadata] for more information.
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-09-26
 */
class MetadataCacheProxy(private val delegate: MetadataResolver) : MetadataResolver {

    init {
        LoggerFactory.getLogger("Cache").info("Scanning database and caching objects (tables, columns, etc)")
        MetadataCache.cache = ConnectionPool.startConnection().use(delegate::findDatabaseMetadata)
    }

    /**
     * Scans database using JDBC API to load partitions and tables metadata
     *
     * @param connection JDBC connection
     */
    override fun findDatabaseMetadata(connection: Connection): DatabaseMetadata =
        MetadataCache.cache

    /**
     * Locates database and JDBC driver product details
     *
     * @param connection JDBC connection
     */
    override fun findProductDetails(connection: Connection): ProductsDetails =
        with(MetadataCache.cache) { ProductsDetails(database, driver) }

    /**
     * Scans database using JDBC API to load partitions details
     *
     * @param connection JDBC connection
     */
    override fun findPartitions(connection: Connection): Map<String, Partition> =
        MetadataCache.cache.partitions

    /**
     * Checks if a partition with provided name exists
     *
     * @param connection JDBC connection
     * @param partitionName Partition name
     */
    override fun partitionExists(connection: Connection, partitionName: String) =
        MetadataCache.cache.partitions.containsKey(partitionName)

    /**
     * Checks if a table with provided name exists
     *
     * @param connection JDBC connection
     * @param partitionName Partition name
     * @param tableName Table name
     */
    override fun tableExists(connection: Connection, partitionName: String, tableName: String) =
        MetadataCache.cache.partitions[partitionName]?.tables?.containsKey(tableName) ?: false

    /**
     * Locate the primary key column names for given [tableName]
     *
     * @param connection JDBC connection
     * @param partitionName Partition name
     * @param tableName Table name
     */
    override fun findTablePrimaryKeyColumns(connection: Connection, partitionName: String, tableName: String) =
        MetadataCache.cache.partitions[partitionName]?.tables?.get(tableName)?.primaryKeyColumns ?: emptySet()

    /**
     * Loads all column details for given [tableName]
     *
     * @param connection JDBC connection
     * @param partitionName Partition name
     * @param tableName Table name
     */
    override fun findTableColumns(connection: Connection, partitionName: String, tableName: String) =
        MetadataCache.cache.partitions[partitionName]?.tables?.get(tableName)?.columns ?: emptyMap()

    /**
     * Loads column details for given [columnName]
     *
     * @param connection JDBC connection
     * @param partitionName Partition name
     * @param tableName Table name
     * @param columnName Column name
     */
    override fun findTableColumn(connection: Connection, partitionName: String, tableName: String, columnName: String): Column? =
        MetadataCache.cache.partitions[partitionName]?.tables?.get(tableName)?.columns?.get(columnName)
}