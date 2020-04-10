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
    private fun resolve(): DriverType {
        val sgbd = EnvironmentVariables.jdbcUrl.split(":")[1].toLowerCase()
        return when(sgbd) {
            in listOf("mysql", "mariadb") -> DriverType.MYSQL
            "postgresql" -> DriverType.POSTGRESQL
            "sqlserver" -> DriverType.SQL_SERVER
            "oracle" -> DriverType.ORACLE
            else -> error("Unsupported SGBD: $sgbd")
        }
    }

}