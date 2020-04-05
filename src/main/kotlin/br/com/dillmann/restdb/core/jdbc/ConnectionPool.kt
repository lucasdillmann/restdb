package br.com.dillmann.restdb.core.jdbc

import br.com.dillmann.restdb.core.environment.EnvironmentVariables
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.pool.HikariPool
import com.zaxxer.hikari.pool.HikariPool.POOL_SHUTDOWN
import java.sql.Connection

/**
 * JDBC connection pool facade
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
object ConnectionPool {

    private val delegate: HikariPool by lazy { startConnectionPool() }

    /**
     * Creates and returns a JDBC connection
     *
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-03-27
     */
    fun startConnection(): Connection =
        delegate.connection

    /**
     * Starts a JDBC connection pool using HikariCP as implementation
     *
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-03-27
     */
    private fun startConnectionPool(): HikariPool {
        logger.info(
            "Starting JDBC connection pool (${EnvironmentVariables.jdbcMaximumConnections} slots) " +
                    "with ${EnvironmentVariables.jdbcUrl}"
        )

        val config = HikariConfig().apply {
            jdbcUrl = EnvironmentVariables.jdbcUrl
            username = EnvironmentVariables.jdbcUsername
            password = EnvironmentVariables.jdbcPassword
            maximumPoolSize = EnvironmentVariables.jdbcMaximumConnections
            minimumIdle = 1
            isAutoCommit = false
        }

        val pool = HikariPool(config)
        pool.shutdownOnJvmStop()
        return pool
    }

    /**
     * Creates and register a [Thread] to shutdown the [HikariPool] automatically when
     * JVM starts to shutdown
     *
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-03-27
     */
    private fun HikariPool.shutdownOnJvmStop() {
        val shutdownThread = Thread {
            if (poolState != POOL_SHUTDOWN) {
                logger.info("JVM shutdown signal received. Stopping JDBC connection pool...")
                shutdown()
                logger.info("Connection pool shutdown complete")
            }
        }

        Runtime.getRuntime().addShutdownHook(shutdownThread)
    }
}