package br.com.dillmann.restdb.domain.metadata.resolver.impl

import br.com.dillmann.restdb.domain.metadata.model.DatabaseMetadata
import br.com.dillmann.restdb.domain.metadata.model.Product
import br.com.dillmann.restdb.domain.metadata.model.ProductsDetails
import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolver
import java.sql.Connection

/**
 * Abstract [MetadataResolver] implementation with common-use logic
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-10
 */
abstract class BasicMetadataResolver : MetadataResolver {

    /**
     * Scans database using JDBC API to load partitions and tables metadata
     *
     * @param connection JDBC connection
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-03-27
     */
    override fun findDatabaseMetadata(connection: Connection): DatabaseMetadata =
        with(connection.metaData) {
            val productsDetails = findProductDetails(connection)
            DatabaseMetadata(
                database = productsDetails.database,
                driver = productsDetails.driver,
                partitions = findPartitions(connection)
            )
        }

    /**
     * Locates database and JDBC driver product details
     *
     * @param connection JDBC connection
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-04-01
     */
    override fun findProductDetails(connection: Connection): ProductsDetails {
        val metadata = connection.metaData
        val database = Product(
            metadata.databaseProductName,
            metadata.databaseProductVersion
        )
        val driver = Product(
            metadata.driverName,
            metadata.driverVersion
        )
        return ProductsDetails(database, driver)
    }

}