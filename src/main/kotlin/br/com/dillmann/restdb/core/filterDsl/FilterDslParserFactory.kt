package br.com.dillmann.restdb.core.filterDsl

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

/**
 * Factory object for [FilterDslParser] instances
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
object FilterDslParserFactory {

    /**
     * Produces an [FilterDslParser] instance for given [input] String with default application error listener
     */
    fun build(input: String): FilterDslParser {
        val inputStream = CharStreams.fromString(input)
        val lexer = FilterDslLexer(inputStream)
        val tokenStream = CommonTokenStream(lexer)
        val parser = FilterDslParser(tokenStream)
        parser.removeErrorListeners()
        parser.removeParseListeners()
        parser.addErrorListener(FilterDslErrorListener)
        return parser
    }
}