package br.com.dillmann.restdb.core.filterDsl.jdbc

import br.com.dillmann.restdb.core.filterDsl.jdbc.exception.InvalidParameterCountException
import br.com.dillmann.restdb.core.filterDsl.jdbc.filter.*
import br.com.dillmann.restdb.core.filterDsl.jdbc.filter.FilterType.*
import br.com.dillmann.restdb.core.filterDsl.jdbc.group.GroupJdbcPredicate
import br.com.dillmann.restdb.core.filterDsl.jdbc.logicalOperator.AndLogicalOperatorJdbcPredicate
import br.com.dillmann.restdb.core.filterDsl.jdbc.logicalOperator.LogicalOperatorJdbcPredicate
import br.com.dillmann.restdb.core.filterDsl.jdbc.logicalOperator.LogicalOperatorType
import br.com.dillmann.restdb.core.filterDsl.jdbc.logicalOperator.OrLogicalOperatorJdbcPredicate

/**
 * [JdbcPredicate] factory
 *
 * This class is able to produce [JdbcPredicate] instances with a simplified access point
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
object JdbcPredicateFactory {

    /**
     * Produces a [FilterJdbcPredicate] for provided [type], [columnName] and [parameters]
     * @throws InvalidParameterCountException when [parameters] size is not compatible with filter [type]
     */
    fun filter(
        type: String,
        columnName: String,
        parameters: List<Any>
    ): FilterJdbcPredicate {
        val filterType = FilterType.fromIdentifier(type)
        validateParametersSize(parameters.size, filterType, columnName)

        return when (filterType) {
            EQUALS -> EqualsFilterJdbcPredicate(columnName, parameters.first())
            NOT_EQUALS -> NotEqualsFilterJdbcPredicate(columnName, parameters.first())
            LESS_THAN -> LessThanFilterJdbcPredicate(columnName, parameters.first())
            LESS_OR_EQUALS -> LessOrEqualsFilterJdbcPredicate(columnName, parameters.first())
            BIGGER_THAN -> BiggerThanFilterJdbcPredicate(columnName, parameters.first())
            BIGGER_OR_EQUALS -> BiggerOrEqualsFilterJdbcPredicate(columnName, parameters.first())
            BETWEEN -> BetweenFilterJdbcPredicate(columnName, parameters[0], parameters[1])
            NOT_BETWEEN -> NotBetweenFilterJdbcPredicate(columnName, parameters[0], parameters[1])
            IN -> InFilterJdbcPredicate(columnName, parameters)
            NOT_IN -> NotInFilterJdbcPredicate(columnName, parameters)
            LIKE -> LikeFilterJdbcPredicate(columnName, parameters.first())
            NOT_LIKE -> NotLikeFilterJdbcPredicate(columnName, parameters.first())
            IS_NULL -> IsNullFilterJdbcPredicate(columnName)
            IS_NOT_NULL -> IsNotNullFilterJdbcPredicate(columnName)
            IS_EMPTY -> IsEmptyFilterJdbcPredicate(columnName)
            IS_NOT_EMPTY -> IsNotEmptyFilterJdbcPredicate(columnName)
        }
    }

    private fun validateParametersSize(currentSize: Int, filter: FilterType, column: String) {
        val exactMode = filter.minimumParameters == filter.maximumParameters
        val notifyError = { discriminator: String, expectedSize: Int ->
            throw InvalidParameterCountException(filter.identifier, column, currentSize, expectedSize, discriminator)
        }

        if (exactMode && currentSize != filter.minimumParameters)
            notifyError("exactly", filter.minimumParameters)

        if (currentSize < filter.minimumParameters)
            notifyError("at least", filter.minimumParameters)

        if (currentSize > filter.maximumParameters)
            notifyError("less than", filter.maximumParameters)
    }

    /**
     * Produces a [LogicalOperatorJdbcPredicate] for provided [type], [leftPredicate] and [rightPredicate]
     */
    fun logicalOperator(
        type: String,
        leftPredicate: JdbcPredicate,
        rightPredicate: JdbcPredicate
    ): LogicalOperatorJdbcPredicate {
        return when (LogicalOperatorType.fromIdentifier(type)) {
            LogicalOperatorType.AND -> AndLogicalOperatorJdbcPredicate(leftPredicate, rightPredicate)
            LogicalOperatorType.OR -> OrLogicalOperatorJdbcPredicate(leftPredicate, rightPredicate)
        }
    }

    /**
     * Produces a [GroupJdbcPredicate] using provided [child]
     */
    fun group(child: JdbcPredicate) =
        GroupJdbcPredicate(child)

}