package br.com.dillmann.restdb.domain.data.get.page

import br.com.dillmann.restdb.domain.data.get.page.sorting.SortColumn

/**
 * Paginated select result details
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
data class Page(
    val partitionName: String,
    val tableName: String,
    val pageNumber: Long,
    val pageSize: Long,
    val pageCount: Long,
    val pageElementsCount: Long,
    val totalElementsCount: Long,
    val firstPage: Boolean,
    val lastPage: Boolean,
    val sorting: Set<SortColumn>,
    val projection: Set<String>,
    val elements: List<Map<String, Any?>>
)