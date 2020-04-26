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
    val jdbcMaximumConnections by env(JDBC_MAXIMUM_CONNECTIONS_DEFAULT_VALUE) { it.toInt() }
    val serverPort by env(SERVER_PORT_DEFAULT_VALUE) { it.toInt() }
    val serverHost by env(SERVER_HOST_DEFAULT_VALUE)
    val enableRequestTracing by env(ENABLE_REQUEST_TRACING_DEFAULT_VALUE) { it.toBoolean() }
    val enableCors by env(ENABLE_CORS_DEFAULT_VALUE) { it.toBoolean() }
    val logLevel by env(LOG_LEVEL_DEFAULT_VALUE)
    val applicationVersion by env(APPLICATION_VERSION_DEFAULT_VALUE)
}