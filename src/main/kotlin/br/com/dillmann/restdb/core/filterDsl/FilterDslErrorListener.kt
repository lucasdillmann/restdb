package br.com.dillmann.restdb.core.filterDsl

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer

/**
 * Filter DSL error listener implementation able to translate parsing errors to an internal application exception.
 * Produced exception will be later on translated to an HTTP response with error details.
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
object FilterDslErrorListener : BaseErrorListener() {

    /**
     * Notifies listener about an Filter DSL syntax error. Always throws an [FilterDslSyntaxException] with
     * received error details.
     */
    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ): Nothing {
        throw FilterDslSyntaxException(
            line = line,
            position = charPositionInLine,
            description = msg,
            symbol = offendingSymbol
        )
    }
}