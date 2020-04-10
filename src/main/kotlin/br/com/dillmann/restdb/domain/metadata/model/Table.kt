package br.com.dillmann.restdb.domain.metadata.model

/**
 * Table metadata details
 *
 * @param name Table name
 * @param primaryKeyColumns Primary key column names
 * @param columns Table columns details
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
data class Table(
    val name: String,
    val primaryKeyColumns: Set<String>,
    val columns: Map<String, Column>
)