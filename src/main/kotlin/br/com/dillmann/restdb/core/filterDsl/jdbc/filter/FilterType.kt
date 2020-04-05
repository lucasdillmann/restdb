package br.com.dillmann.restdb.core.filterDsl.jdbc.filter

/**
 * Enumeration of compatible JDBC filter operations and related parameters metadata
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
enum class FilterType(
    val identifier: String,
    val minimumParameters: Int,
    val maximumParameters: Int
) {
    EQUALS(
        identifier = "equals",
        minimumParameters = 1,
        maximumParameters = 1
    ),
    NOT_EQUALS(
        identifier = "notEquals",
        minimumParameters = 1,
        maximumParameters = 1
    ),
    LESS_THAN(
        identifier = "lessThan",
        minimumParameters = 1,
        maximumParameters = 1
    ),
    LESS_OR_EQUALS(
        identifier = "lessOrEquals",
        minimumParameters = 1,
        maximumParameters = 1
    ),
    BIGGER_THAN(
        identifier = "biggerThan",
        minimumParameters = 1,
        maximumParameters = 1
    ),
    BIGGER_OR_EQUALS(
        identifier = "biggerOrEquals",
        minimumParameters = 1,
        maximumParameters = 1
    ),
    BETWEEN(
        identifier = "between",
        minimumParameters = 2,
        maximumParameters = 2
    ),
    NOT_BETWEEN(
        identifier = "notBetween",
        minimumParameters = 2,
        maximumParameters = 2
    ),
    IN(
        identifier = "in",
        minimumParameters = 1,
        maximumParameters = 99
    ),
    NOT_IN(
        identifier = "notIn",
        minimumParameters = 1,
        maximumParameters = 99
    ),
    LIKE(
        identifier = "like",
        minimumParameters = 1,
        maximumParameters = 1
    ),
    NOT_LIKE(
        identifier = "notLike",
        minimumParameters = 1,
        maximumParameters = 1
    ),
    IS_NULL(
        identifier = "isNull",
        minimumParameters = 0,
        maximumParameters = 0
    ),
    IS_NOT_NULL(
        identifier = "isNotNull",
        minimumParameters = 0,
        maximumParameters = 0
    ),
    IS_EMPTY(
        identifier = "isEmpty",
        minimumParameters = 0,
        maximumParameters = 0
    ),
    IS_NOT_EMPTY(
        identifier = "isNotEmpty",
        minimumParameters = 0,
        maximumParameters = 0
    );

    companion object {
        /**
         * Finds the [FilterType] compatible with the provided [identifier]
         * @throws NoSuchElementException when no match is found
         */
        fun fromIdentifier(identifier: String) =
            values().first { it.identifier == identifier }
    }
}