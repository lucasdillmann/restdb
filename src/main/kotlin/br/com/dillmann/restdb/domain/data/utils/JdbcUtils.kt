package br.com.dillmann.restdb.domain.data.utils

import br.com.dillmann.restdb.core.jdbc.isNumeric
import br.com.dillmann.restdb.domain.metadata.findTableColumns
import java.sql.Array
import java.sql.Connection

/**
 * Selects a single row in database
 *
 * @param connection JDBC connection
 * @param schema Schema name
 * @param table Table name
 * @param tableColumns All table columns
 * @param primaryKeyColumns Table primary key column names
 * @param data Primary key values
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
fun retrieveSingleRow(
    connection: Connection,
    schema: String,
    table: String,
    primaryKeyColumns: Set<String>,
    tableColumns: Set<String>,
    data: Map<String, Any?>
): Map<String, Any?>? {

    val columns = findTableColumns(connection, schema, table)
    val primaryKeyValues = primaryKeyColumns.map {
        val value = data[it]
        val column = columns[it] ?: error("Missing column metadata for $schema.$table.$it")
        if (column.jdbcType.isNumeric() && value is String) value.toDouble() else value
    }
    val statementColumns = primaryKeyColumns.joinToString(separator = ", ") { "$it = ?" }
    val selectSqlStatement = "SELECT * FROM $schema.$table WHERE $statementColumns"

    connection.prepareStatement(selectSqlStatement).use { statement ->
        primaryKeyValues.forEachIndexed { index, value -> statement.setParameter(index + 1, value) }
        statement.executeQuery().use { resultSet ->
            return@retrieveSingleRow if (resultSet.next()) {
                tableColumns.map { it to resultSet.getObject(it).autoConvertArray() }.toMap()
            } else {
                null
            }
        }
    }
}

/**
 * When JDBC result object is an array, automatically converts it to an kotlin [Array] instance. When it is not,
 * the original instance is returned.
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
fun Any?.autoConvertArray(): Any? =
    if (this is Array) array
    else this
