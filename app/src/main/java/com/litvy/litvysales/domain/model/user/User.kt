package com.litvy.litvysales.domain.model.user

data class User(
    val id: Int = 0,
    val name: String,
    val lastname: String,
    val telephoneNumber: String?,
    val dni: String?,
    val birthDate: Long?,
    val address: String?,
    val email: String?,
    val passwordHash: String,

    val roleId: Int,
    val active: Boolean,

    val createdAt: Long,
    val updatedAt: Long
)
