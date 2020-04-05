package br.com.dillmann.restdb.domain.metadata

/**
 * Schema metadata details
 *
 * @param name Schema name
 * @param tables Children table details
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
data class Schema(
    val name: String,
    val tables: Map<String, Table>
)