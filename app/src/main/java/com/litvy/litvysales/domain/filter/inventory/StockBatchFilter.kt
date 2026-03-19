package com.litvy.litvysales.domain.filter.inventory

import com.litvy.litvysales.domain.filter.common.QuerySortDirection

data class StockBatchFilter(
    val productId: Int? = null,
    val purchaseItemId: Int? = null,
    val expirationDateFrom: Long? = null,
    val expirationDateTo: Long? = null,
    val createdAtFrom: Long? = null,
    val createdAtTo: Long? = null,
    val sortBy: StockBatchSortBy = StockBatchSortBy.CREATED_AT,
    val sortDirection: QuerySortDirection = QuerySortDirection.DESC,
    val limit: Int? = null,
    val offset: Int? = null
)

enum class StockBatchSortBy {
    CREATED_AT,
    EXPIRATION_DATE,
    PRODUCT_ID
}
