package com.litvy.litvysales.domain.filter.inventory

import com.litvy.litvysales.domain.filter.common.QuerySortDirection
import com.litvy.litvysales.domain.model.enums.StockMovementType

data class StockMovementFilter(
    val productId: Int? = null,
    val batchId: Int? = null,
    val type: StockMovementType? = null,
    val createdAtFrom: Long? = null,
    val createdAtTo: Long? = null,
    val referenceId: Int? = null,
    val referenceType: String? = null,
    val createdBy: Int? = null,
    val sortBy: StockMovementSortBy = StockMovementSortBy.CREATED_AT,
    val sortDirection: QuerySortDirection = QuerySortDirection.DESC,
    val limit: Int? = null,
    val offset: Int? = null
)

enum class StockMovementSortBy {
    CREATED_AT,
    PRODUCT_ID,
    TYPE
}
