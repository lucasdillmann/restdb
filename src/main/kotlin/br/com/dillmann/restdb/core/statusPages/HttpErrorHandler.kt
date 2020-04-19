package br.com.dillmann.restdb.core.statusPages

import br.com.dillmann.restdb.core.statusPages.handlers.*
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.StatusPages

/**
 * Extension function for [Application] for Ktor exception handling
 *
 * This function configures and installs exception handlers for the Ktor framework able to convert an application error
 * to an user-friendly http response.
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-26
 */
fun Application.installErrorHandlers() {
    install(StatusPages) {
        validationExceptionHandler()
        sqlExceptionHandler()
        filterDslSyntaxExceptionHandler()
        httpExceptionHandler()
        jsonProcessingExceptionHandler()
    }
}