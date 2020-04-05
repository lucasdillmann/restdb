package br.com.dillmann.restdb.core.filterDsl.converter

/**
 * Conversion tree node types enumeration
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-30
 */
enum class NodeType {
    /**
     * Tree root node
     */
    ROOT,

    /**
     * Group of expressions
     */
    GROUP,

    /**
     * Filter expression
     */
    FILTER,

    /**
     * Logical operator expression
     */
    LOGICAL_OPERATOR
}