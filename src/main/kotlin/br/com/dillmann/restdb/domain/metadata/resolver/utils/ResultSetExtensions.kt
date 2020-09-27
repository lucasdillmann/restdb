package br.com.dillmann.restdb.domain.metadata.resolver.utils

import br.com.dillmann.restdb.domain.metadata.model.Column
import java.sql.JDBCType
import java.sql.ResultSet

/**
 * Reads the [ResultSet] to extract column metadata
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-10
 */
fun ResultSet.columns(): Map<String, Column> =
    use { resultSet ->
        val columns = mutableMapOf<String, Column>()
        while (resultSet.next()) {
            val column = resultSet.column()
            columns += column.name to column
        }

        columns
    }

/**
 * Reads current [ResultSet] position to extract column metadata
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-10
 */
fun ResultSet.column(): Column {
    val name = getString("column_name")
    val typeId = getInt("data_type")

    return Column(
        name = name,
        typeId = typeId,
        typeName = getString("type_name"),
        jdbcType = runCatching { JDBCType.valueOf(typeId) }.getOrElse { JDBCType.OTHER },
        size = getLong("column_size"),
        decimalDigits = getLong("decimal_digits"),
        nullable = getString("is_nullable").let(::parseBoolean),
        autoIncrement = getString("is_autoincrement").let(::parseBoolean),
        ordinalPosition = getInt("ordinal_position")
    )
}

private fun parseBoolean(input: String) =
    listOf("true", "yes", "y", "1").any { it.equals(input, ignoreCase = true) }

/**
 * Reads the [ResultSet] to extract table primary keys
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-10
 */
fun ResultSet.tablePrimaryKeys(): Set<String> =
    use { resultSet ->
        val result = mutableSetOf<String>()
        while (resultSet.next()) {
            result += resultSet.getString("column_name")
        }

        result
    }
