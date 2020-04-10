package br.com.dillmann.restdb.domain.metadata.model

/**
 * Partition metadata details
 *
 * @param name Partition name
 * @param tables Children table details
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
data class Partition(
    val name: String,
    val tables: Map<String, Table>
)