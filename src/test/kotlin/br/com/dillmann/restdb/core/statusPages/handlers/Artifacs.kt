package br.com.dillmann.restdb.core.statusPages.handlers

import br.com.dillmann.restdb.core.filterDsl.FilterDslSyntaxException
import br.com.dillmann.restdb.core.statusPages.exceptions.ValidationException
import br.com.dillmann.restdb.core.statusPages.exceptions.randomErrorDetailsList
import br.com.dillmann.restdb.testUtils.randomPositiveInt
import br.com.dillmann.restdb.testUtils.randomString
import br.com.dillmann.restdb.testUtils.randomUuid
import io.ktor.http.HttpStatusCode

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun randomFilterDslSyntaxException() =
    FilterDslSyntaxException(randomPositiveInt(), randomPositiveInt(), randomUuid(), randomString())

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun randomHttpStatusCode() =
    HttpStatusCode.allStatusCodes.random()

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun randomValidationException() =
    ValidationException(randomErrorDetailsList())