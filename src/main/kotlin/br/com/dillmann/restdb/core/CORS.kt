package br.com.dillmann.restdb.core

import br.com.dillmann.restdb.core.environment.EnvironmentVariables
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.http.HttpMethod
import org.slf4j.LoggerFactory
import java.time.Duration

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
private val logger = LoggerFactory.getLogger("CORS")

/**
 * Configures CORS in application if enabled by environment variables
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-12
 */
fun Application.installCors() {
    if (EnvironmentVariables.enableCors) {
        install(CORS) {
            anyHost()
            HttpMethod.DefaultMethods.forEach(::method)
            allowCredentials = true
            allowSameOrigin = true
            maxAgeInSeconds = Duration.ofMinutes(1).seconds
        }

        logger.info("Cross-Origin Resource Sharing enabled with all sources allowed")
    }
}