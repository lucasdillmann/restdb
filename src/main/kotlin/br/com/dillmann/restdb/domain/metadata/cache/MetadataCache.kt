package br.com.dillmann.restdb.domain.metadata.cache

import br.com.dillmann.restdb.domain.metadata.model.DatabaseMetadata

/**
 * Simple, in-memory [DatabaseMetadata] cache
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-09-26
 */
object MetadataCache {
    lateinit var cache: DatabaseMetadata
}