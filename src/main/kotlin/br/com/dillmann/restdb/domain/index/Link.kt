package br.com.dillmann.restdb.domain.index

/**
 * HATEOAS link representation
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-25
 */
data class Link(val rel: String, val href: String)