package br.com.dillmann.restdb.domain.data.utils

import java.sql.JDBCType
import java.sql.PreparedStatement

/**
 * Detects the [value] type and calls the correct value setter from [PreparedStatement]
 *
 * @param index Parameter index
 * @param value Value to be set
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
fun PreparedStatement.setParameter(index: Int, value: Any?) {
    when (value) {
        is Collection<*> -> setArrayParameter(index, value.toTypedArray())
        is Array<*> -> setArrayParameter(index, value)
        else -> setObject(index, value)
    }
}

/**
 * Creates a SQL array for the given [value] and set it as [PreparedStatement] parameter
 *
 * @param index Parameter index
 * @param value Array value to be set
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
private fun PreparedStatement.setArrayParameter(index: Int, value: Array<*>) {
    val containsNumbersOnly = value.all { it is Number }
    val arrayType = if (containsNumbersOnly) JDBCType.NUMERIC else JDBCType.VARCHAR
    val sqlArray = connection.createArrayOf(arrayType.name, value)
    setArray(index, sqlArray)
}