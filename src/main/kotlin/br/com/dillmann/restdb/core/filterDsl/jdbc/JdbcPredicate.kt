package br.com.dillmann.restdb.core.filterDsl.jdbc

/**
 * JDBC predicate definition interface
 *
 * This interface defines the minimum contract for Filter DSL SQL instructions, allowing dynamic, user provided
 * filters for paginated queries
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
interface JdbcPredicate {
    /**
     * SQL instructions
     */
    val sqlInstructions: String

    /**
     * SQL dynamic parameters names and values
     */
    val parameters: Map<String, Any>

    /**
     * Columns used by [sqlInstructions]
     */
    val columns: Set<String>
}
