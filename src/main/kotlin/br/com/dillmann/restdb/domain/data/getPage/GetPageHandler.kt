package br.com.dillmann.restdb.domain.data.getPage

import br.com.dillmann.restdb.domain.data.getPage.filter.asFilterPredicate
import br.com.dillmann.restdb.domain.data.getPage.projection.asProjectionInstructions
import br.com.dillmann.restdb.domain.data.getPage.sorting.asSortingInstructions
import br.com.dillmann.restdb.domain.data.utils.getQueryParameter
import br.com.dillmann.restdb.domain.data.utils.mainRequestParameters
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

private const val DEFAULT_PAGE_NUMBER = 0L
private const val DEFAULT_PAGE_SIZE = 25L

/**
 * Handles GET requests for a page of database rows
 *
 * @param call Application call
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
suspend fun handleGetPage(call: ApplicationCall) {
    val (partitionName, tableName) = call.mainRequestParameters()
    val pageNumber = call.getQueryParameter("pageNumber", DEFAULT_PAGE_NUMBER) { it.toLong() }
    val pageSize = call.getQueryParameter("pageSize", DEFAULT_PAGE_SIZE) { it.toLong() }
    val sorting = call.getQueryParameter("sort", "").asSortingInstructions()
    val projection = call.getQueryParameter("columns", "").asProjectionInstructions()
    val filter = call.getQueryParameter("filter", "").asFilterPredicate()

    val page = findPage(partitionName, tableName, pageNumber, pageSize, sorting, projection, filter)
    call.respond(HttpStatusCode.OK, page)
}