package br.com.dillmann.restdb.core.log

import br.com.dillmann.restdb.core.environment.EnvironmentVariables
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Level.*
import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory

/**
 * SLF4J log level configuration utility
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-01
 */
object LogLevelConfiguration {

    /**
     * Configures application root logger level using value from environment variable
     *
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-04-01
     */
    fun configure() {
        val rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
        rootLogger.level = loadLogLevel()
    }

    /**
     * Loads log level from environment variable
     *
     * @throws IllegalArgumentException when environment value is not a valid value
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-04-01
     */
    private fun loadLogLevel(): Level =
        toLevel(EnvironmentVariables.logLevel, null)
            ?: error("Invalid log level ${EnvironmentVariables.logLevel}. Allowed options: ${allowedLevels()}.")

    /**
     * Generates a [String] representation of all allowed log levels
     *
     * @author Lucas Dillmann
     * @since 1.0.0, 2020-04-01
     */
    private fun allowedLevels() =
        listOf(OFF, ERROR, WARN, INFO, DEBUG, TRACE, ALL).joinToString { it.levelStr }
}
