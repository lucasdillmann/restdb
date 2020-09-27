package br.com.dillmann.restdb.domain.data.utils

import br.com.dillmann.restdb.core.converter.ValueConverter
import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolverFactory
import java.sql.Array
import java.sql.Connection

/**
 * Selects a single row in database
 *
 * @param connection JDBC connection
 * @param partition Partition name
 * @param table Table name
 * @param tableColumns All table columns
 * @param primaryKeyColumns Table primary key column names
 * @param data Primary key values
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
fun retrieveSingleRow(
    connection: Connection,
    partition: String,
    table: String,
    primaryKeyColumns: Set<String>,
    tableColumns: Set<String>,
    data: Map<String, Any?>
): Map<String, Any?>? {

    val columns = MetadataResolverFactory.build().findTableColumns(connection, partition, table)
    val primaryKeyValues = primaryKeyColumns.map {
        val value = data[it]
        val column = columns[it] ?: error("Missing column metadata for $partition.$table.$it")
        if (value is String) ValueConverter.convert(value, partition, table, column.name) else value
    }
    val statementColumns = primaryKeyColumns.joinToString(separator = ", ") { "$it = ?" }
    val selectSqlStatement = "SELECT * FROM $partition.$table WHERE $statementColumns"

    return connection.prepareStatement(selectSqlStatement).use { statement ->
        primaryKeyValues.forEachIndexed { index, value -> statement.setParameter(index + 1, value) }
        statement.executeQuery().use { resultSet ->
            if (resultSet.next()) {
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
