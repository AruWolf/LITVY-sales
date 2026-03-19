package com.litvy.litvysales.domain.filter.sales

import com.litvy.litvysales.domain.filter.common.QuerySortDirection

data class CashMovementFilter(
    val cashSessionId: Int? = null,
    val type: String? = null,
    val createdAtFrom: Long? = null,
    val createdAtTo: Long? = null,
    val createdBy: Int? = null,
    val sortBy: CashMovementSortBy = CashMovementSortBy.CREATED_AT,
    val sortDirection: QuerySortDirection = QuerySortDirection.DESC,
    val limit: Int? = null,
    val offset: Int? = null
)

enum class CashMovementSortBy {
    CREATED_AT,
    AMOUNT_IN_CENTS,
    TYPE
}
