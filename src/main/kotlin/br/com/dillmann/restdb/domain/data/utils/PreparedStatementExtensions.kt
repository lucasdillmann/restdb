package br.com.dillmann.restdb.domain.data.utils

import br.com.dillmann.restdb.core.converter.ValueConverter
import br.com.dillmann.restdb.domain.data.exception.InvalidColumnNameException
import br.com.dillmann.restdb.domain.metadata.model.Column
import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolverFactory
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
fun PreparedStatement.setRawParameter(index: Int, value: Any?) {
    when (value) {
        is Collection<*> -> setArrayParameter(index, value.toTypedArray())
        is Array<*> -> setArrayParameter(index, value)
        else -> setObject(index, value)
    }
}

/**
 * Detects the [value] type and calls the correct value setter from [PreparedStatement]
 *
 * @param index Parameter index
 * @param value Value to be set
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
fun PreparedStatement.setParameter(index: Int, value: Any?, column: Column) {
    when (value) {
        is Collection<*> -> setArrayParameter(index, value.toTypedArray())
        is Array<*> -> setArrayParameter(index, value)
        else -> convertAndSetValue(index, value, column)
    }
}

/**
 * Detects the [value] type and calls the correct value setter from [PreparedStatement]
 *
 * @param index Parameter index
 * @param value Value to be set
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
fun PreparedStatement.setParameter(index: Int, value: Any?, partitionName: String, tableName: String, columnName: String) {
    val column = MetadataResolverFactory
        .build()
        .findTableColumn(connection, partitionName, tableName, columnName)
        ?: throw InvalidColumnNameException(partitionName, tableName, columnName)

    setParameter(index, value, column)
}

private fun PreparedStatement.convertAndSetValue(index: Int, value: Any?, column: Column) {
    if (value == null) {
        setNull(index, column.typeId)
        return
    }
    val convertedValue = ValueConverter.convert(value.toString(), column)
    setObject(index, convertedValue)
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