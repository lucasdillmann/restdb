package br.com.dillmann.restdb.domain.data.getPage.filter

import br.com.dillmann.restdb.core.filterDsl.FilterDslParserFacade
import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate

/**
 * Converts a [String] into a [JdbcPredicate] using Filter DSL parser
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-30
 */
fun String.asFilterPredicate() =
    takeIf { isNotBlank() }?.let { FilterDslParserFacade.parse(it) }