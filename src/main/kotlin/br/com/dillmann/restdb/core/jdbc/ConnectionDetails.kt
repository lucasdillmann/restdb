package br.com.dillmann.restdb.core.jdbc

import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolverFactory

/**
 * Utility object for informative connection state details
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-01
 */
object ConnectionDetails {

    /**
     * Loads product details (database's and driver's name and version) and print they using logger
     *
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-04-01
     */
    fun printProductDetails() {
        val metadataResolver = MetadataResolverFactory.build()
        val (database, driver) = ConnectionPool.startConnection().use(metadataResolver::findProductDetails)
        logger.info("Connection started successfully to database $database using $driver")
    }
}