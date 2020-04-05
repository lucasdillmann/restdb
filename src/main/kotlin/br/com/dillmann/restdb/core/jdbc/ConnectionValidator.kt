package br.com.dillmann.restdb.core.jdbc

import java.sql.Connection
import kotlin.random.Random
import kotlin.system.exitProcess

/**
 * JDBC connection test utility
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
object ConnectionValidator {

    /**
     * Checks if a connection can be made to database using JDBC, stopping application when connection cant be made
     *
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-03-27
     */
    fun checkJdbcConnectionState() {
        try {
            logger.info("Checking if application can reach database over JDBC")
            ConnectionPool.startConnection().use(::runTestQuery)
        } catch (ex: Exception) {
            logger.error("Application failed asserting JDBC connection to database", ex)
            exitProcess(1)
        }
    }

    /**
     * Executes a test query into provided [connection] and validates if the result is what is expected. When query fails
     * or received result is different from what is expected, an error is thrown.
     *
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-03-27
     */
    private fun runTestQuery(connection: Connection) {
        val expectedValue = Random.nextLong()
        val statement = connection.prepareStatement("SELECT $expectedValue")
        statement.execute()
        statement.resultSet.use { resultSet ->
            val result = resultSet?.takeIf { it.next() }?.getLong(1)
            check(expectedValue == result) {
                "Connection test failed with incorrect query result '$result'. Expected was '$expectedValue'."
            }
        }
    }
}