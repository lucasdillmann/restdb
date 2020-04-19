package br.com.dillmann.restdb.domain.data.get.page.jdbc

import br.com.dillmann.restdb.core.jdbc.type.DriverType
import br.com.dillmann.restdb.core.jdbc.type.DriverTypeResolver

/**
 * Builder object for SQL offset instructions compatible with current SGBD syntax
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-10
 */
object OffsetSyntaxBuilder {

    private val producer by lazy { resolveSyntax() }

    /**
     * Generates an offset SQL compatible with current SGBD
     */
    fun build(offset: Long, pageSize: Long): String =
        producer(offset, pageSize)

    /**
     * Detects current SGBD and returns a compatible SQL generator
     */
    private fun resolveSyntax(): (Long, Long) -> String =
        when (DriverTypeResolver.current()) {
            DriverType.MYSQL -> { offset, pageSize -> "LIMIT $offset, $pageSize" }
            DriverType.POSTGRESQL -> { offset, pageSize -> "OFFSET $offset FETCH NEXT $pageSize ROWS ONLY" }
            DriverType.SQL_SERVER -> { offset, pageSize -> "OFFSET $offset ROWS FETCH NEXT $pageSize ROWS ONLY" }
        }

}