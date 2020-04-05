package br.com.dillmann.restdb.core.environment

/**
 * Converts a [String] using camel case format to a [String] with snake case
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-04
 */
fun String.camelCaseToSnakeCase(): String {
    val uppercaseRange = 'A'..'Z'
    val snakeCase = fold(StringBuilder(length)) { accumulator, character ->
        if (character in uppercaseRange)
            accumulator.let { if (it.isNotEmpty()) it.append('_') else it }.append(character + ('a' - 'A'))
        else
            accumulator.append(character)
    }

    return snakeCase.toString()
}