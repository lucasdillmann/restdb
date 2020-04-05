package br.com.dillmann.restdb.domain.metadata

import br.com.dillmann.restdb.core.jdbc.ConnectionPool
import java.sql.Connection
import java.sql.JDBCType

/**
 * Scans database using JDBC API to load schemas and tables metadata
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
fun findDatabaseMetadata(): DatabaseMetadata =
    ConnectionPool.startConnection().use(::findDatabaseMetadata)

/**
 * Scans database using JDBC API to load schemas and tables metadata
 *
 * @param connection JDBC connection
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
fun findDatabaseMetadata(connection: Connection): DatabaseMetadata =
    with(connection.metaData) {
        val productsDetails = findProductDetails(connection)
        DatabaseMetadata(
            database = productsDetails.database,
            driver = productsDetails.driver,
            schemas = findSchemas(connection)
        )
    }

/**
 * Locates database and JDBC driver product details
 *
 * @param connection JDBC connection
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-01
 */
fun findProductDetails(connection: Connection): ProductsDetails {
    val metadata = connection.metaData
    val database = Product (metadata.databaseProductName, metadata.databaseProductVersion)
    val driver = Product (metadata.driverName, metadata.driverVersion)
    return ProductsDetails(database, driver)
}

/**
 * Scans database using JDBC API to load schemas details
 *
 * @param connection JDBC connection
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
private fun findSchemas(connection: Connection): Map<String, Schema> {
    connection.metaData.schemas
        .use { resultSet ->
            val schemas = mutableMapOf<String, Schema>()
            while (resultSet.next()) {
                val name =
                    resultSet.getString("table_schem") // it is not a typo, jdbc uses "table_schem" as result column name
                schemas += name to Schema(
                    name = name,
                    tables = findSchemaTables(connection, name)
                )
            }

            return@findSchemas schemas
        }
}

/**
 * Scans database using JDBC API to load table details from given [schemaName]
 *
 * @param connection JDBC connection
 * @param schemaName Schema name
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
private fun findSchemaTables(connection: Connection, schemaName: String): Map<String, Table> {
    connection.metaData.getTables(null, schemaName, null, arrayOf("TABLE"))
        .use { resultSet ->
            val tables = mutableMapOf<String, Table>()
            while (resultSet.next()) {
                val name = resultSet.getString("table_name")
                tables += name to Table(
                    name = name,
                    primaryKeyColumns = findTablePrimaryKeyColumns(connection, schemaName, name),
                    columns = findTableColumns(connection, schemaName, name)
                )
            }

            return@findSchemaTables tables
        }
}

/**
 * Locate the primary key column names for given [tableName]
 *
 * @param connection JDBC connection
 * @param schemaName Schema name
 * @param tableName Table name
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
fun findTablePrimaryKeyColumns(connection: Connection, schemaName: String, tableName: String): Set<String> {
    connection.metaData.getPrimaryKeys(null, schemaName, tableName)
        .use { resultSet ->
            val result = mutableSetOf<String>()
            while (resultSet.next()) {
                result += resultSet.getString("column_name")
            }
            return@findTablePrimaryKeyColumns result
        }
}

/**
 * Loads all column details for given [tableName]
 *
 * @param connection JDBC connection
 * @param schemaName Schema name
 * @param tableName Table name
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
fun findTableColumns(connection: Connection, schemaName: String, tableName: String): Map<String, Column> {
    connection.metaData.getColumns(null, schemaName, tableName, null)
        .use { resultSet ->
            val columns = mutableMapOf<String, Column>()
            while (resultSet.next()) {
                val name = resultSet.getString("column_name")
                val typeId = resultSet.getInt("data_type")
                columns += name to Column(
                    name = name,
                    typeId = typeId,
                    typeName = resultSet.getString("type_name"),
                    jdbcType = JDBCType.valueOf(typeId),
                    size = resultSet.getLong("column_size"),
                    decimalDigits = resultSet.getLong("decimal_digits"),
                    nullable = resultSet.getBoolean("is_nullable"),
                    autoIncrement = resultSet.getBoolean("is_autoincrement"),
                    ordinalPosition = resultSet.getInt("ordinal_position")
                )
            }

            return@findTableColumns columns
        }
}