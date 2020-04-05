package br.com.dillmann.restdb.core.filterDsl.jdbc.logicalOperator

/**
 * Enumeration of compatible JDBC logical operators
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
enum class LogicalOperatorType(val identifier: String) {
    OR("||"),
    AND("&&");

    companion object {
        /**
         * Finds the [LogicalOperatorType] compatible with the provided [identifier]
         * @throws NoSuchElementException when no match is found
         */
        fun fromIdentifier(identifier: String) =
            values().first { it.identifier == identifier }
    }
}