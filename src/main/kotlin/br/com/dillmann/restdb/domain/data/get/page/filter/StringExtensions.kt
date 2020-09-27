package br.com.dillmann.restdb.domain.data.get.page.filter

import br.com.dillmann.restdb.core.filterDsl.FilterDslParserFacade
import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate

/**
 * Converts a [String] into a [JdbcPredicate] using Filter DSL parser
 *
 * @param partitionName Partition name
 * @param tableName Table name
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-30
 */
fun String.asFilterPredicate(partitionName: String, tableName: String) =
    takeIf { isNotBlank() }?.let { FilterDslParserFacade.parse(it, partitionName, tableName) }