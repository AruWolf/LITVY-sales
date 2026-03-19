package com.litvy.litvysales.domain.filter.sales

import com.litvy.litvysales.domain.filter.common.QuerySortDirection

data class CashRegisterFilter(
    val name: String? = null,
    val location: String? = null,
    val active: Boolean? = null,
    val sortBy: CashRegisterSortBy = CashRegisterSortBy.NAME,
    val sortDirection: QuerySortDirection = QuerySortDirection.ASC
)

enum class CashRegisterSortBy {
    NAME,
    LOCATION,
    ACTIVE
}
