package br.com.dillmann.restdb.domain.data.getPage.jdbc

import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate
import br.com.dillmann.restdb.domain.data.getPage.exception.InvalidColumnNamesException
import br.com.dillmann.restdb.domain.data.getPage.exception.InvalidPageNumberException
import br.com.dillmann.restdb.domain.data.getPage.exception.InvalidPageSizeException
import br.com.dillmann.restdb.domain.data.getPage.sorting.SortColumn

/**
 * Validates if the SQL can be generated successfully with parameters from user request
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-30
 */
object SqlBuilderValidator {

    /**
     * Executes the validation
     *
     * @param allColumns All database column names
     * @param projection User provided list of columns to be returned
     * @param sorting User provided sorting instructions
     * @param pageNumber User provided page number
     * @param pageSize User provided page size
     * @param filter User provided filter instructions
     * @throws InvalidColumnNamesException when any unknown column is provided
     * @throws InvalidPageNumberException when an invalid page number is provided
     * @throws InvalidPageSizeException when an invalid page size is provided
     */
    fun validate(
        allColumns: Set<String>,
        projection: Set<String>?,
        sorting: Set<SortColumn>?,
        pageNumber: Long,
        pageSize: Long,
        filter: JdbcPredicate?
    ) {
        if (projection != null)
            validateUnknownColumns(allColumns, projection)
        if (sorting != null)
            validateUnknownColumns(allColumns, sorting.map { it.columnName }.toSet())
        if (filter != null)
            validateUnknownColumns(allColumns, filter.columns)

        validatePage(pageNumber, pageSize)

    }

    /**
     * Validates if the [pageNumber] and [pageSize] are valid
     */
    private fun validatePage(pageNumber: Long, pageSize: Long) {
        if (pageNumber < 0)
            throw InvalidPageNumberException(
                pageNumber
            )

        if (pageSize < 0)
            throw InvalidPageSizeException(
                pageSize
            )
    }

    /**
     * Validates if the user provided column names exists
     */
    private fun validateUnknownColumns(allColumns: Set<String>, receivedColumns: Set<String>) {
        val unknownColumns = receivedColumns - allColumns
        if (unknownColumns.isNotEmpty())
            throw InvalidColumnNamesException(
                unknownColumns
            )
    }
}