package br.com.dillmann.restdb.domain.metadata.model

/**
 * Database metadata details
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
data class DatabaseMetadata(
    val database: Product,
    val driver: Product,
    val partitions: Map<String, Partition>
)




