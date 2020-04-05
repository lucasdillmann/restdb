package br.com.dillmann.restdb.core

import br.com.dillmann.restdb.core.environment.EnvironmentVariables
import br.com.dillmann.restdb.module
import io.ktor.application.Application
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

/**
 * Ktor embedded server facade
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
object EmbeddedServer {

    /**
     * Configures and starts Ktor embedded Netty server
     *
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-03-28
     */
    fun start() {
        val environment = applicationEngineEnvironment {
            module(Application::module)
            connector {
                port = EnvironmentVariables.serverPort
                host = EnvironmentVariables.serverHost
            }
        }

        embeddedServer(Netty, environment).start(wait = true)
    }
}