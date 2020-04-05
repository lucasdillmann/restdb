package br.com.dillmann.restdb.domain.metadata

/**
 * Generic product details
 *
 * @param name Product name
 * @param version Product version
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
data class Product(
    val name: String,
    val version: String
) {
    override fun toString() = "$name $version"
}