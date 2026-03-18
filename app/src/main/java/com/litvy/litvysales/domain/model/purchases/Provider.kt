package com.litvy.litvysales.domain.model.purchases

data class Provider(
    val id: Int?,
    val name: String,
    val cuit: String?,
    val telephoneNumber: String?,
    val address: String?,
    val email: String?
)
