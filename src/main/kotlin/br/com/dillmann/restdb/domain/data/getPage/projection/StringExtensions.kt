package br.com.dillmann.restdb.domain.data.getPage.projection

/**
 * Converts a [String] into SQL column names using comma as delimiter/separator, producing a [Set] of [String] as
 * result
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
fun String.asProjectionInstructions() =
    takeUnless { isBlank() }?.split(",")?.toSet()