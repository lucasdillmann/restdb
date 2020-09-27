package br.com.dillmann.restdb.domain.metadata.resolver

import br.com.dillmann.restdb.core.environment.EnvironmentVariables
import br.com.dillmann.restdb.core.jdbc.type.DriverType
import br.com.dillmann.restdb.core.jdbc.type.DriverTypeResolver
import br.com.dillmann.restdb.domain.metadata.cache.MetadataCacheProxy
import br.com.dillmann.restdb.domain.metadata.resolver.impl.CatalogBasedMetadataResolver
import br.com.dillmann.restdb.domain.metadata.resolver.impl.SchemaBasedMetadataResolver

/**
 * Factory object for [MetadataResolver]
 *
 * This class is able to detect and return a compatible [MetadataResolver] for current JDBC driver and target database
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-10
 */
object MetadataResolverFactory {

    private val compatibleImplementation by lazy { resolveCompatibleImplementation() }

    /**
     * Builds and returns a compatible [MetadataResolver] with current JDBC driver
     */
    fun build(): MetadataResolver =
        compatibleImplementation

    private fun resolveCompatibleImplementation(): MetadataResolver {
        val implementation = when (DriverTypeResolver.current()) {
            DriverType.MYSQL -> CatalogBasedMetadataResolver
            DriverType.POSTGRESQL -> SchemaBasedMetadataResolver
            DriverType.SQL_SERVER -> SchemaBasedMetadataResolver
        }

        return if (EnvironmentVariables.cacheDatabaseMetadata) MetadataCacheProxy(implementation) else implementation
    }

}