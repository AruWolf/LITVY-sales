package com.litvy.litvysales.domain.filter.purchases

import com.litvy.litvysales.domain.filter.common.QuerySortDirection

data class PurchaseFilter(
    val providerId: Int? = null,
    val invoiceTypeId: Int? = null,
    val paymentMethodId: Int? = null,
    val createdAtFrom: Long? = null,
    val createdAtTo: Long? = null,
    val createdBy: Int? = null,
    val sortBy: PurchaseSortBy = PurchaseSortBy.CREATED_AT,
    val sortDirection: QuerySortDirection = QuerySortDirection.DESC,
    val limit: Int? = null,
    val offset: Int? = null
)

enum class PurchaseSortBy {
    CREATED_AT,
    TOTAL_IN_CENTS,
    PROVIDER_ID
}
