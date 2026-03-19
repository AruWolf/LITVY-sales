package com.litvy.litvysales.domain.filter.purchases

import com.litvy.litvysales.domain.filter.common.QuerySortDirection

data class ProviderFilter(
    val name: String? = null,
    val cuit: String? = null,
    val telephoneNumber: String? = null,
    val address: String? = null,
    val email: String? = null,
    val sortBy: ProviderSortBy = ProviderSortBy.NAME,
    val sortDirection: QuerySortDirection = QuerySortDirection.ASC,
    val limit: Int? = null,
    val offset: Int? = null
)

enum class ProviderSortBy {
    NAME,
    CUIT,
    EMAIL
}
