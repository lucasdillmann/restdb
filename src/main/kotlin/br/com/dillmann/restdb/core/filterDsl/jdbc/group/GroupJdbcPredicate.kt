package br.com.dillmann.restdb.core.filterDsl.jdbc.group

import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate

/**
 * [JdbcPredicate] implementation for Filter DSL expression groups
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
class GroupJdbcPredicate(child: JdbcPredicate) : JdbcPredicate {

    /**
     * SQL instructions
     */
    override val sqlInstructions = "(${child.sqlInstructions})"

    /**
     * SQL dynamic parameters names and values
     */
    override val parameters = child.parameters

    /**
     * Columns used by [sqlInstructions]
     */
    override val columns = child.columns
}