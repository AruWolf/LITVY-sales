package com.litvy.litvysales.domain.filter.user

import com.litvy.litvysales.domain.filter.common.QuerySortDirection

data class UserFilter(
    val name: String? = null,
    val lastName: String? = null,
    val telephoneNumber: String? = null,
    val dni: String? = null,
    val birthDate: Long? = null,
    val address: String? = null,
    val email: String? = null,
    val roleId: Int? = null,
    val active: Boolean? = null,
    val createdAtFrom: Long? = null,
    val createdAtTo: Long? = null,
    val updatedAtFrom: Long? = null,
    val updatedAtTo: Long? = null,
    val sortBy: UserSortBy = UserSortBy.CREATED_AT,
    val sortDirection: QuerySortDirection = QuerySortDirection.DESC
)

enum class UserSortBy {
    CREATED_AT,
    UPDATED_AT,
    NAME,
    LASTNAME
}
