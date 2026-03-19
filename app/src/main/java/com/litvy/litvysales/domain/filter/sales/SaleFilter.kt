package com.litvy.litvysales.domain.filter.sales

import com.litvy.litvysales.domain.filter.common.QuerySortDirection
import com.litvy.litvysales.domain.model.enums.SaleStatus

data class SaleFilter(
    val cashSessionId: Int? = null,
    val sellerId: Int? = null,
    val customerId: Int? = null,
    val status: SaleStatus? = null,
    val createdAtFrom: Long? = null,
    val createdAtTo: Long? = null,
    val sortBy: SaleSortBy = SaleSortBy.CREATED_AT,
    val sortDirection: QuerySortDirection = QuerySortDirection.DESC,
    val limit: Int? = null,
    val offset: Int? = null
)

enum class SaleSortBy {
    CREATED_AT,
    TOTAL_IN_CENTS,
    STATUS
}
