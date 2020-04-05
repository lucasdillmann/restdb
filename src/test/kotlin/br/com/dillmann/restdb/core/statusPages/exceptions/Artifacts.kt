package br.com.dillmann.restdb.core.statusPages.exceptions

import br.com.dillmann.restdb.core.statusPages.utils.ErrorDetails
import br.com.dillmann.restdb.testUtils.randomString

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun randomErrorDetailsList() =
    listOf(randomErrorDetails())

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun randomErrorDetails() =
    ErrorDetails(randomString(), randomString(), randomString())