package br.com.dillmann.restdb.core.statusPages.utils

import java.sql.SQLException

/**
 * Converts an [SQLException] to a [List] of [ErrorDetails]. Conversion is done
 * recursively where each child in [SQLException#nextException] generates an [ErrorDetails].
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
fun SQLException.details(): List<ErrorDetails> {
    val details = mutableListOf<ErrorDetails>()
    var detailsException: SQLException? = this
    while (detailsException != null) {
        details += ErrorDetails(
            message = detailsException.message,
            sqlState = detailsException.sqlState
        )
        detailsException = detailsException.nextException
    }

    return details
}