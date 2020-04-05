package br.com.dillmann.restdb

import br.com.dillmann.restdb.core.EmbeddedServer
import br.com.dillmann.restdb.core.jdbc.ConnectionDetails
import br.com.dillmann.restdb.core.jdbc.ConnectionValidator
import br.com.dillmann.restdb.core.log.LogLevelConfiguration

/**
 * JVM application entry point
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
fun main() {
    LogLevelConfiguration.configure()
    ConnectionValidator.checkJdbcConnectionState()
    ConnectionDetails.printProductDetails()
    EmbeddedServer.start()
}