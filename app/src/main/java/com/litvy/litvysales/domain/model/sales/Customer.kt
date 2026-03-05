package com.litvy.litvysales.domain.model.sales

data class Customer(
    val id: Int = 0,

    val name: String,
    val lastname: String?,

    val cuit: String?,

    val telephoneNumber: String?,
    val address: String?,
    val email: String?,

    val createdAt: Long
)
