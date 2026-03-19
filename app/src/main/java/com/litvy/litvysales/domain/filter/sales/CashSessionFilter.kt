package com.litvy.litvysales.domain.filter.sales

import com.litvy.litvysales.domain.filter.common.QuerySortDirection
import com.litvy.litvysales.domain.model.enums.CashSessionStatus

data class CashSessionFilter(
    val registerId: Int? = null,
    val status: CashSessionStatus? = null,
    val openedBy: Int? = null,
    val closedBy: Int? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val isOpen: Boolean? = null,
    val sortBy: CashSessionSortBy = CashSessionSortBy.STARTED_AT,
    val sortDirection: QuerySortDirection = QuerySortDirection.DESC,
    val limit: Int? = null,
    val offset: Int? = null
)

enum class CashSessionSortBy {
    STARTED_AT,
    CLOSED_AT,
    STATUS
}
