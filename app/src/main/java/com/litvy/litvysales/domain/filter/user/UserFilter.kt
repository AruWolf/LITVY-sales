package com.litvy.litvysales.domain.filter.user

data class UserFilter(
    val name: String? = null,
    val lastName: String? = null,
    val telephoneNumber: String? = null,
    val dni: String? = null,
    val birthDate: Long? = null,
    val address: String? = null,
    val email: String? = null,
    val roleId: Int? = null,
    val active: Boolean = true,
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)
