package br.com.dillmann.restdb.core.filterDsl

import br.com.dillmann.restdb.core.filterDsl.converter.FilterDslJdbcConverter
import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate

/**
 * Facade class for simple Filter DSL parsing and conversion to [JdbcPredicate]
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
object FilterDslParserFacade {

    /**
     * Parses the given [input] String into a [JdbcPredicate]
     */
    fun parse(input: String): JdbcPredicate {
        val converter = FilterDslJdbcConverter()
        val parser = FilterDslParserFactory.build(input)
        parser.addParseListener(converter)
        parser.root() // starts the parse at the root element

        return converter.jdbcPredicate()
    }
}