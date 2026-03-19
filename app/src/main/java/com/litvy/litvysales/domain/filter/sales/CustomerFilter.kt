package com.litvy.litvysales.domain.filter.sales

import com.litvy.litvysales.domain.filter.common.QuerySortDirection

data class CustomerFilter(
    val name: String? = null,
    val lastname: String? = null,
    val cuit: String? = null,
    val telephoneNumber: String? = null,
    val address: String? = null,
    val email: String? = null,
    val createdAtFrom: Long? = null,
    val createdAtTo: Long? = null,
    val sortBy: CustomerSortBy = CustomerSortBy.CREATED_AT,
    val sortDirection: QuerySortDirection = QuerySortDirection.DESC,
    val limit: Int? = null,
    val offset: Int? = null
)

enum class CustomerSortBy {
    CREATED_AT,
    NAME,
    LASTNAME
}
