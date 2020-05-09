package br.com.dillmann.restdb.core.jdbc.type

import br.com.dillmann.restdb.core.environment.EnvironmentVariables

/**
 * [DriverType] resolver
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-10
 */
object DriverTypeResolver {

    private val currentType by lazy { resolve() }

    /**
     * Detects and returns current SGBD in use
     */
    fun current(): DriverType =
        currentType

    /**
     * Resolves current SGBD type in use
     */
    private fun resolve(): DriverType =
        when (val database = EnvironmentVariables.jdbcUrl.split(":")[1].toLowerCase()) {
            in listOf("mysql", "mariadb") -> DriverType.MYSQL
            "postgresql" -> DriverType.POSTGRESQL
            "sqlserver" -> DriverType.SQL_SERVER
            else -> error("Unsupported database: $database")
        }

}