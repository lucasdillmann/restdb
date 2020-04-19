package br.com.dillmann.restdb.domain.metadata.openapi

import java.sql.JDBCType
import java.sql.JDBCType.*

/**
 * Converts a [JDBCType] to a OpenAPI JSON attribute type name
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-19
 */
fun JDBCType.toJsonType(): String =
    when(this) {
        TINYINT, SMALLINT, INTEGER, BIGINT -> "integer"
        FLOAT, REAL, DOUBLE, NUMERIC, DECIMAL -> "number"
        BOOLEAN -> "boolean"
        ARRAY -> "array"
        JAVA_OBJECT -> "object"
        else -> "string"
    }

/**
 * Converts a [JDBCType] to a OpenAPI JSON attribute format name
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-19
 */
fun JDBCType.toJsonFormat(): String? =
    when(this) {
        TINYINT, SMALLINT, INTEGER -> "int32"
        BIGINT -> "int64"
        FLOAT, DECIMAL -> "float"
        REAL, DOUBLE, NUMERIC -> "double"
        BIT -> "byte"
        BINARY, VARBINARY, LONGVARBINARY, NCLOB, BLOB, CLOB -> "binary"
        TIME -> "time"
        DATE -> "date"
        TIMESTAMP, TIME_WITH_TIMEZONE, TIMESTAMP_WITH_TIMEZONE -> "date-time"
        else -> null
    }