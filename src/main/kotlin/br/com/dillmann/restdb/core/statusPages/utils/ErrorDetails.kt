package br.com.dillmann.restdb.core.statusPages.utils

/**
 * Validation violation details
 *
 * @param column Database column name
 * @param message Message or error description
 * @param sqlState SQL state number
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
data class ErrorDetails(
    val column: String? = null,
    val message: String? = null,
    val sqlState: String? = null
)