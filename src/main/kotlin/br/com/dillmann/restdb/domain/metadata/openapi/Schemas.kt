package br.com.dillmann.restdb.domain.metadata.openapi

import br.com.dillmann.restdb.domain.metadata.model.Column
import br.com.dillmann.restdb.domain.metadata.model.DatabaseMetadata
import br.com.dillmann.restdb.domain.metadata.model.Partition
import br.com.dillmann.restdb.domain.metadata.model.Table
import io.swagger.v3.oas.models.media.ArraySchema
import io.swagger.v3.oas.models.media.Schema
import java.sql.JDBCType

/**
 * Kotlin typealias of a [List] of a [Pair] of [String] to [Schema], used to simplify code verbosity
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
private typealias Schemas = List<Pair<String, Schema<*>>>

/**
 * Converts a [DatabaseMetadata] into a [Map] of [String] to OpenAPI [Schema] object
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
fun DatabaseMetadata.asOpenApiSchemas(): Map<String, Schema<*>> {
    val errorSchema = "error_details" to buildErrorResponseSchema()
    val tablesSchemas = partitions.values.flatMap(::buildSchema).toMap()
    return tablesSchemas + errorSchema
}

/**
 * Builds and returns a [Schema] for standard error responses
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
private fun buildErrorResponseSchema(): Schema<*> {
    val statusCode = buildErrorResponseSchema("statusCode", JDBCType.INTEGER)
    val statusDescription = buildErrorResponseSchema("statusDescription", JDBCType.VARCHAR)
    val uri = buildErrorResponseSchema("uri", JDBCType.VARCHAR)
    val message = buildErrorResponseSchema("message", JDBCType.VARCHAR)
    val details = buildErrorResponseSchema("details", JDBCType.JAVA_OBJECT)

    return Schema<Any>().also {
        it.type = "object"
        it.properties = listOf(statusCode, statusDescription, uri, message, details)
            .map { attribute -> attribute.name to attribute }
            .toMap()
    }
}

/**
 * Builds and returns an attribute [Schema] for standard error responses
 *
 * @param name Attribute name
 * @param type Attribute type
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
private fun buildErrorResponseSchema(name: String, type: JDBCType) =
    Schema<Any>().also {
        it.name = name
        it.type = type.toJsonType()
        it.format = type.toJsonFormat()
    }

/**
 * Builds a [Schemas] object for the provided [partition], where each element represents a [Table]
 *
 * @param partition Database partition details
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
private fun buildSchema(partition: Partition): Schemas =
    partition.tables.values.flatMap { buildSchema(partition.name, it) }.toList()

/**
 * Builds a [Schemas] object for the provided [table]
 *
 * @param partitionName Database partition name
 * @param table Database table details
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
private fun buildSchema(partitionName: String, table: Table): Schemas {
    val baseName = "${partitionName}_${table.name}"
    val singleRowSchema = Schema<Any>().also {
        it.name = "data_$baseName"
        it.type = "object"
        it.properties = table.columns.values.map(::buildSchema).toMap()
    }
    val pagedSchema = buildPageSchema(baseName)

    return listOf(
        "data_$baseName" to singleRowSchema,
        "paged_data_$baseName" to pagedSchema
    )
}

/**
 * Builds an OpenAPI [Schema] object of a paginated contract for the requested [baseName] single row contract
 *
 * @param baseName Single row schema name
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-19
 */
private fun buildPageSchema(baseName: String): Schema<*> {
    data class Attribute(val name: String, val type: JDBCType, val subtype: JDBCType? = null)
    val pageProperties = listOf(
        Attribute(name = "partitionName", type = JDBCType.VARCHAR),
        Attribute(name = "tableName", type = JDBCType.VARCHAR),
        Attribute(name = "pageNumber", type = JDBCType.INTEGER),
        Attribute(name = "pageSize", type = JDBCType.INTEGER),
        Attribute(name = "pageCount", type = JDBCType.INTEGER),
        Attribute(name = "pageElementsCount", type = JDBCType.INTEGER),
        Attribute(name = "totalElementsCount", type = JDBCType.INTEGER),
        Attribute(name = "totalElementsCount", type = JDBCType.INTEGER),
        Attribute(name = "firstPage", type = JDBCType.BOOLEAN),
        Attribute(name = "lastPage", type = JDBCType.BOOLEAN),
        Attribute(name = "sorting", type = JDBCType.ARRAY, subtype = JDBCType.VARCHAR),
        Attribute(name = "projection", type = JDBCType.ARRAY, subtype = JDBCType.VARCHAR)
    ).map { (name, type, subtype) ->
        val schema: Schema<Any> = if (type == JDBCType.ARRAY)
            ArraySchema().also {
                it.items = Schema<Any>()
                it.items.type = subtype!!.toJsonType()
                it.items.format = subtype.toJsonFormat()
            }
        else
            Schema()

        schema.name = name
        schema.type = type.toJsonType()
        schema.format = type.toJsonFormat()
        name to schema
    }.toMutableList()

    pageProperties += "elements" to ArraySchema().also {
        it.name = "elements"
        it.type = JDBCType.ARRAY.toJsonType()
        it.items = Schema<Any>()
        it.items.`$ref` = "#/components/schemas/data_$baseName"
    }

    return Schema<Any>().also {
        it.name = "paged_data_$baseName"
        it.type = "object"
        it.properties = pageProperties.toMap()
    }
}

/**
 * Build and returns an OpenAPI attribute [Schema] object for the provided [column]
 *
 * @param column Database column details
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
private fun buildSchema(column: Column): Pair<String, Schema<*>> {
    val schema = ArraySchema().also {
        it.type = column.jdbcType.toJsonType()
        it.format = column.jdbcType.toJsonFormat()
        it.name = column.name
        it.nullable = column.nullable
        it.maxLength = column.size?.toInt()
        it.extensions = mapOf(
            "sqlTypeId" to column.typeId,
            "jdbcType" to column.jdbcType,
            "autoIncrement" to column.autoIncrement,
            "ordinalPosition" to column.ordinalPosition,
            "decimalDigits" to column.decimalDigits
        )

        if (column.jdbcType == JDBCType.ARRAY)
            it.items = Schema<Any>()
    }

    return column.name to schema
}
