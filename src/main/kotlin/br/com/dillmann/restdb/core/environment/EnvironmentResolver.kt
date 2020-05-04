package br.com.dillmann.restdb.core.environment

/**
 * Centralized environment variables resolver
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-05-02
 */
object EnvironmentResolver {

    /**
     * Provides a [Map] of [String] to [String] with all environment variables values
     */
    fun values(): Map<String, String> =
        System.getenv()

}