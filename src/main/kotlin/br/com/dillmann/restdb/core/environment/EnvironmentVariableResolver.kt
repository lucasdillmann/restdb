package br.com.dillmann.restdb.core.environment

import kotlin.reflect.KProperty

/**
 * Creates a Kotlin property delegate able to resolve environment variables. Resolution will be done
 * using property name as the environment variable name (after conversion of the camel case to snake case).
 *
 * @param defaultValue Default value when user do not provide one
 * @param valueConverter Value converter from [String] to [T]
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-17
 */
fun <T : Any> env(defaultValue: T? = null, valueConverter: (String) -> T) =
    EnvironmentVariableResolver(defaultValue, valueConverter)

/**
 * Creates a Kotlin [String] property delegate able to resolve environment variables. Resolution will be done
 * using property name as the environment variable name (after conversion of the camel case to snake case).
 *
 * @param defaultValue Default value when user do not provide one
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-17
 */
fun env(defaultValue: String? = null) =
    env(defaultValue) { it }

/**
 * Kotlin delegate property resolver for environment variables
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-04
 */
class EnvironmentVariableResolver<T : Any>(
    private val defaultValue: T?,
    private val converter: (String) -> T
) {
    private lateinit var value: T

    /**
     * @param reference Instance reference
     * @param property Property details
     */
    operator fun getValue(reference: Any?, property: KProperty<*>): T {
        if (!::value.isInitialized) {
            val variableName = property.name.camelCaseToSnakeCase().toUpperCase()
            val environmentKey = "$ENVIRONMENT_VARIABLES_PREFIX$variableName"
            value = System.getenv().entries
                .firstOrNull { (key, _) -> environmentKey.equals(key, ignoreCase = true) }
                ?.let { (_, value) -> converter(value) }
                ?: defaultValue ?: error("Missing environment variable: $variableName")
        }

        return value
    }
}

