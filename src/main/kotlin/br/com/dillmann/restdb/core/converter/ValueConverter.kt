package br.com.dillmann.restdb.core.converter

import br.com.dillmann.restdb.core.jdbc.ConnectionPool
import br.com.dillmann.restdb.core.statusPages.exceptions.ValidationException
import br.com.dillmann.restdb.core.statusPages.utils.ErrorDetails
import br.com.dillmann.restdb.domain.metadata.model.Column
import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolverFactory
import java.net.InetAddress
import java.sql.JDBCType
import java.sql.JDBCType.*
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.util.*

/**
 * Data type converter
 *
 * This utility object converts values from String to corresponding Java types expected by JDBC for any
 * given column.
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-09-26
 */
object ValueConverter {

    /**
     * Detects the type of the provided [columnName] and tries to convert the [input] value
     * to the corresponding Java type of the column
     *
     * @param input Input value
     * @param partitionName Partition name
     * @param tableName Table name
     * @param columnName Column name
     */
    fun convert(input: String, partitionName: String, tableName: String, columnName: String): Any =
        ConnectionPool
            .startConnection()
            .use { MetadataResolverFactory.build().findTableColumn(it, partitionName, tableName, columnName) }
            ?.let { convert(input, it) }
            ?: input

    /**
     * Detects the type of the provided [column] and tries to convert the [input] value
     * to the corresponding Java type of the column
     *
     * @param input Input value
     * @param column Column metadata
     */
    fun convert(input: String, column: Column): Any =
        try {
            selectConverter(column)(input)
        } catch (ex: Exception) {
            val errors = listOf(
                ErrorDetails(
                    column = column.name,
                    message = "Application was not able to convert provided value to column's database value type",
                    sqlState = null
                )
            )

            throw ValidationException(errors)
        }

    private fun selectConverter(column: Column): (String) -> Any =
        runCatching { selectConverterByJdbcType(column.jdbcType) }
            .getOrElse { selectConverterByNativeType(column.typeName) }

    private fun selectConverterByJdbcType(type: JDBCType): (String) -> Any =
        when(type) {
            TINYINT, SMALLINT, INTEGER -> {{ it.toInt() }}
            BIGINT -> {{ it.toBigInteger() }}
            FLOAT, DECIMAL -> {{ it.toFloat() }}
            REAL, DOUBLE, NUMERIC -> {{ it.toDouble() }}
            BINARY, VARBINARY, LONGVARBINARY, NCLOB, BLOB, CLOB -> {{ it.toByteArray() }}
            TIME -> {{ OffsetTime.parse(it) }}
            DATE -> {{ LocalDate.parse(it) }}
            TIMESTAMP, TIME_WITH_TIMEZONE, TIMESTAMP_WITH_TIMEZONE -> {{ OffsetDateTime.parse(it) }}
            CHAR, NCHAR -> {{ it.toCharArray().first() }}
            VARCHAR, LONGVARCHAR, NVARCHAR, LONGNVARCHAR -> {{ it }}
            BIT, BOOLEAN -> {{ it.toBoolean() }}
            else -> error("Unsupported data type: $type")
        }

    private fun selectConverterByNativeType(type: String): (String) -> Any =
        when (type.trim().toLowerCase()) {
            "uuid" -> {{ UUID.fromString(it) }}
            "inet", "cidr" -> {{ InetAddress.getByAddress(it.toByteArray()) }}
            else -> {{ it }}
        }

}