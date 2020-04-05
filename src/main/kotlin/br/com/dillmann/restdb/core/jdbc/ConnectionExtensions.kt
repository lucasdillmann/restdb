package br.com.dillmann.restdb.core.jdbc

import java.sql.Connection

/**
 * Executes the provided [action] inside a JDBC transaction, committing the result at the end and, if any exception is
 * thrown, rolls-back the changes.
 *
 * @param action Action to be executed
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
fun Connection.runInTransaction(action: () -> Unit) {
    try {
        beginRequest()
        action()
        commit()
    } catch (ex: Exception) {
        rollback()
        throw ex
    }
}