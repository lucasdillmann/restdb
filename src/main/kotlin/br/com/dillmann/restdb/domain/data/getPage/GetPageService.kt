package br.com.dillmann.restdb.domain.data.getPage

import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate
import br.com.dillmann.restdb.core.jdbc.ConnectionPool
import br.com.dillmann.restdb.domain.data.getPage.jdbc.SqlBuilder
import br.com.dillmann.restdb.domain.data.getPage.jdbc.escapeSql
import br.com.dillmann.restdb.domain.data.getPage.sorting.SortColumn
import br.com.dillmann.restdb.domain.data.utils.autoConvertArray
import br.com.dillmann.restdb.domain.data.utils.setParameter
import br.com.dillmann.restdb.domain.data.validateSchemaAndTableName
import br.com.dillmann.restdb.domain.metadata.findTableColumns
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Executes a SELECT operation in database provided [schemaName] and [tableName] using a paginated strategy
 *
 * @param schemaName Schema name
 * @param tableName Table name
 * @param pageNumber Page number, starting at 0
 * @param pageSize Page size (how much rows per page)
 * @param sorting Sorting instructions
 * @param projection Column names to be returned from database
 * @param filter Row filtering instructions
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
fun findPage(
    schemaName: String,
    tableName: String,
    pageNumber: Long,
    pageSize: Long,
    sorting: Set<SortColumn>?,
    projection: Set<String>?,
    filter: JdbcPredicate?
): Page {
    ConnectionPool.startConnection().use { connection ->
        validateSchemaAndTableName(connection, schemaName, tableName)
        val allColumns = findTableColumns(connection, schemaName, tableName).keys
        val (pageSql, countSql, parameters) =
            SqlBuilder(schemaName, tableName, allColumns, pageSize, pageNumber, projection, sorting, filter).build()

        val totalCount = connection
            .prepareStatement(countSql, parameters)
            .executeQuery()
            .use {
                it.next()
                it.getLong("count")
            }
        val results = connection
            .prepareStatement(pageSql, parameters)
            .executeQuery()
            .getResults(projection ?: allColumns)

        return@findPage Page(
            schemaName = schemaName,
            tableName = tableName,
            pageNumber = pageNumber,
            pageSize = pageSize,
            pageCount = totalCount / pageSize + 1,
            pageElementsCount = results.size.toLong(),
            totalElementsCount = totalCount,
            firstPage = pageNumber == 0L,
            lastPage = pageSize * (pageNumber + 1) >= totalCount,
            sorting = sorting ?: emptySet(),
            projection = projection ?: allColumns,
            elements = results
        )

    }
}

/**
 * Creates a [PreparedStatement] using provided [sql] and [parameters]
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
private fun Connection.prepareStatement(sql: String, parameters: Map<Int, Any?>): PreparedStatement =
    prepareStatement(sql)
        .also { statement ->
            parameters.forEach { (position, value) -> statement.setParameter(position, value) }
        }


private fun <T> Collection<T>.asArrayExpression(): String =
    joinToString(prefix = "[", postfix = "]", separator = ",") { it.toString().escapeSql() }

/**
 * Reads all results from a [ResultSet] using provided column names and returns it
 *
 * @param projection Column names to be read
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
private fun ResultSet.getResults(projection: Set<String>): List<Map<String, Any?>> {
    return use { resultSet ->
        val results = mutableListOf<Map<String, Any?>>()

        while (resultSet.next()) {
            results += resultSet.readColumns(projection)
        }

        results
    }
}

/**
 * Reads a single result from a [ResultSet] using provided column names
 *
 * @param columns Column names to be read
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
private fun ResultSet.readColumns(columns: Set<String>): Map<String, Any?> =
    columns.map { it to getObject(it).autoConvertArray() }.toMap()



