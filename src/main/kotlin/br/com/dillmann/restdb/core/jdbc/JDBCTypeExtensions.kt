package br.com.dillmann.restdb.core.jdbc

import java.sql.JDBCType
import java.sql.JDBCType.*

/**
 * Checks if current [JDBCType] is a numeric type
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun JDBCType.isNumeric() =
    this in listOf(TINYINT, SMALLINT, INTEGER, BIGINT, FLOAT, REAL, DOUBLE, NUMERIC, DECIMAL)