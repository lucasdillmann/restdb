package br.com.dillmann.restdb.core

import br.com.dillmann.restdb.core.environment.EnvironmentVariables
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import org.slf4j.event.Level

/**
 * Extension function for [Application] for Ktor request logging and tracing
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-26
 */
fun Application.installRequestTracing() {
    if (EnvironmentVariables.enableRequestTracing) {
        install(CallLogging) {
            level = Level.INFO
        }
    }
}