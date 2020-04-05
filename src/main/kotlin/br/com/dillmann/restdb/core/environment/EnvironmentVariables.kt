package br.com.dillmann.restdb.core.environment

/**
 * Application environment variables
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
object EnvironmentVariables {
    val jdbcUrl by env()
    val jdbcUsername by env()
    val jdbcPassword by env()
    val jdbcMaximumConnections by env(DEFAULT_JDBC_MAXIMUM_CONNECTIONS) { it.toInt() }
    val serverPort by env(DEFAULT_SERVER_PORT) { it.toInt() }
    val serverHost by env(DEFAULT_SERVER_HOST)
    val enableRequestTracing by env(DEFAULT_ENABLE_REQUEST_TRACING) { it.toBoolean() }
    val logLevel by env(DEFAULT_LOG_LEVEL)
}