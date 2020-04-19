package br.com.dillmann.restdb.domain.data.get.page.jdbc

import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate
import br.com.dillmann.restdb.domain.data.get.page.sorting.SortColumn
import br.com.dillmann.restdb.domain.data.get.page.sorting.SortDirection
import br.com.dillmann.restdb.domain.data.get.page.sorting.asSqlInstructions
import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolverFactory
import java.sql.Connection

/**
 * SQL builder for paginated queries
 *
 * @param connection JDBC Connection
 * @param partition Database partition name
 * @param table Table name
 * @param columns All columns available in the [table] of the [partition]
 * @param pageSize Amount of rows to return in requested page
 * @param pageNumber Page ordinal number
 * @param projection Columns to be selected by the generated query
 * @param sorting Query sorting instructions
 * @param filter Query filter instructions
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-30
 */
class SqlBuilder(
    private val connection: Connection,
    private val partition: String,
    private val table: String,
    private val columns: Set<String>,
    private val pageSize: Long,
    private val pageNumber: Long,
    private val projection: Set<String>?,
    private val sorting: Set<SortColumn>?,
    private val filter: JdbcPredicate?
) {

    /**
     * SQL construction result details
     *
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-03-30
     */
    data class Result(val pageSql: String, val countSql: String, val parameters: Map<Int, Any?>)

    private val paramPattern = ":(param_[a-zA-Z0-9]+)*".toRegex()

    /**
     * Generates the query SQL, row count SQL and the parameters key-values
     */
    fun build(): Result {
        SqlBuilderValidator.validate(columns, projection, sorting, pageNumber, pageSize, filter)
        val (rawPageSql, rawCountSql) = buildRawSql()

        if (filter == null)
            return Result(rawPageSql, rawCountSql, emptyMap())

        val rawParameters = filter.parameters
        val (pageSql, parameters) = replaceNamedParameters(rawPageSql, rawParameters)
        val (countSql, _) = replaceNamedParameters(rawCountSql, rawParameters)
        return Result(pageSql, countSql, parameters)
    }

    /**
     * Generates the raw query and count SQLs
     */
    private fun buildRawSql(): Pair<String, String> {
        val offset = pageNumber * pageSize
        val sortingColumns = sorting ?: defaultSorting(partition, table)
        val sortingSql = sortingColumns.asSqlInstructions()
        val columnsSql = projection?.joinToString() ?: columns.joinToString()
        val filterSql = filter?.sqlInstructions?.let { "WHERE $it" } ?: ""
        val offsetSql = OffsetSyntaxBuilder.build(offset, pageSize)

        val pageSql = """
            SELECT $columnsSql 
            FROM $partition.$table 
            $filterSql 
            $sortingSql 
            $offsetSql
        """.trimIndent().escapeSql()
        val countSql = """
            SELECT COUNT(*) AS count 
            FROM $partition.$table 
            $filterSql
        """.trimIndent().escapeSql()

        return pageSql to countSql
    }

    /**
     * Builds a default sorting instructions using table primary keys when available or all table
     * columns otherwise
     *
     * @param partition Partition name
     * @param table Table name
     */
    private fun defaultSorting(partition: String, table: String): Set<SortColumn> {
        val metadataResolver = MetadataResolverFactory.build()
        val toSorting: Set<String>.() -> Set<SortColumn> = {
            map { SortColumn(it, SortDirection.ASC) }.toSet()
        }

        val primaryKeys = metadataResolver
            .findTablePrimaryKeyColumns(connection, partition, table)
            .toSorting()

        return if (primaryKeys.isNotEmpty()) primaryKeys
        else columns.toSorting()
    }


    /**
     * Replaces the named parameters from SQL with native, JDBC supported value placeholders
     *
     * @param sql SQL to be handled
     * @param parameters Named parameters used in the SQL
     */
    private fun replaceNamedParameters(sql: String, parameters: Map<String, Any?>): Pair<String, Map<Int, Any?>> {
        val resultParameters = mutableMapOf<Int, Any?>()
        var resultSql = sql
        paramPattern
            .findAll(sql)
            .forEachIndexed { index, match ->
                val parameter = match.value
                resultSql = resultSql.replaceFirst(parameter, "?")
                resultParameters[index + 1] = parameters[parameter.substring(1)]
            }

        return resultSql to resultParameters
    }

}