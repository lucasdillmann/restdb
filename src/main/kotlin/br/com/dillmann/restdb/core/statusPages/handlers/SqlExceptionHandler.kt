package br.com.dillmann.restdb.core.statusPages.handlers

import br.com.dillmann.restdb.core.statusPages.logger
import br.com.dillmann.restdb.core.statusPages.utils.details
import br.com.dillmann.restdb.core.statusPages.utils.sendErrorResponse
import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import java.sql.SQLException

/**
 * Status page error handler for [SQLException]
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun StatusPages.Configuration.sqlExceptionHandler() {
    exception<SQLException> { exception ->
        logger.error(exception.message, exception)
        call.sendErrorResponse(
            statusCode = HttpStatusCode.UnprocessableEntity,
            message = "Action rejected by database",
            details = exception.details()
        )
    }
}