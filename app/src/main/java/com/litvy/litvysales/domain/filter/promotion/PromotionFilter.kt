package com.litvy.litvysales.domain.filter.promotion

import com.litvy.litvysales.domain.filter.common.QuerySortDirection

data class PromotionFilter(
    val name: String? = null,
    val priority: Int? = null,
    val stackable: Boolean? = null,
    val active: Boolean? = null,
    val clearStock: Boolean? = null,
    val startDateFrom: Long? = null,
    val startDateTo: Long? = null,
    val endDateFrom: Long? = null,
    val endDateTo: Long? = null,
    val createdAtFrom: Long? = null,
    val createdAtTo: Long? = null,
    val sortBy: PromotionSortBy = PromotionSortBy.CREATED_AT,
    val sortDirection: QuerySortDirection = QuerySortDirection.DESC,
    val limit: Int? = null,
    val offset: Int? = null
)

enum class PromotionSortBy {
    CREATED_AT,
    NAME,
    PRIORITY,
    START_DATE,
    END_DATE
}
