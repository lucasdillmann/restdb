package br.com.dillmann.restdb.domain.metadata

import java.sql.JDBCType

/**
 * Table column metadata details
 *
 * @param name Column name
 * @param typeId SQL column type ID
 * @param typeName SQL column type description
 * @param size Column size/length
 * @param decimalDigits Column decimal digits count, when column is numeric
 * @param nullable Whether column accepts null values or not
 * @param autoIncrement Whether column has auto increment support or not
 * @param ordinalPosition Ordinal position of the column in table
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
data class Column(
    val name: String,
    val typeId: Int,
    val typeName: String,
    val jdbcType: JDBCType,
    val size: Long?,
    val decimalDigits: Long?,
    val nullable: Boolean,
    val autoIncrement: Boolean,
    val ordinalPosition: Int
)

