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
            val name = resultSet.getString("column_name")
            val typeId = resultSet.getInt("data_type")
            columns += name to Column(
                name = name,
                typeId = typeId,
                typeName = resultSet.getString("type_name"),
                jdbcType = JDBCType.valueOf(typeId),
                size = resultSet.getLong("column_size"),
                decimalDigits = resultSet.getLong("decimal_digits"),
                nullable = resultSet.getString("is_nullable").let(::parseBoolean),
                autoIncrement = resultSet.getString("is_autoincrement").let(::parseBoolean),
                ordinalPosition = resultSet.getInt("ordinal_position")
            )
        }

        columns
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
