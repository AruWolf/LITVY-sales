package com.litvy.litvysales.domain.filter.purchases

import com.litvy.litvysales.domain.filter.common.QuerySortDirection
import com.litvy.litvysales.domain.model.enums.PurchaseOrderStatus

data class PurchaseOrderFilter(
    val providerId: Int? = null,
    val status: PurchaseOrderStatus? = null,
    val expectedDeliveryDateFrom: Long? = null,
    val expectedDeliveryDateTo: Long? = null,
    val createdAtFrom: Long? = null,
    val createdAtTo: Long? = null,
    val createdBy: Int? = null,
    val sortBy: PurchaseOrderSortBy = PurchaseOrderSortBy.CREATED_AT,
    val sortDirection: QuerySortDirection = QuerySortDirection.DESC,
    val limit: Int? = null,
    val offset: Int? = null
)

enum class PurchaseOrderSortBy {
    CREATED_AT,
    EXPECTED_DELIVERY_DATE,
    STATUS
}
