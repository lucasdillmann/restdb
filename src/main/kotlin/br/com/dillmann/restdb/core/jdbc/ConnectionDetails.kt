package br.com.dillmann.restdb.core.jdbc

import br.com.dillmann.restdb.domain.metadata.findProductDetails

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
        val (database, driver) = ConnectionPool.startConnection().use(::findProductDetails)
        logger.info("Connection started successfully to database $database using $driver")
    }
}