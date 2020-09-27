package br.com.dillmann.restdb.core.environment

/**
 * Environment variable constants
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-04
 */
const val ENVIRONMENT_VARIABLES_PREFIX = "RESTDB_"
const val JDBC_MAXIMUM_CONNECTIONS_DEFAULT_VALUE = 4
const val SERVER_PORT_DEFAULT_VALUE = 8080
const val SERVER_HOST_DEFAULT_VALUE = "0.0.0.0"
const val LOG_LEVEL_DEFAULT_VALUE = "INFO"
const val APPLICATION_VERSION_DEFAULT_VALUE = "development"
const val ENABLE_REQUEST_TRACING_DEFAULT_VALUE = false
const val ENABLE_CORS_DEFAULT_VALUE = true
const val CACHE_DATABASE_METADATA = true
val EXTERNAL_URL_DEFAULT_VALUE = with(EnvironmentVariables) { "http://$serverHost:$serverPort" }