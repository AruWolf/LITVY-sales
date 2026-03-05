package com.litvy.litvysales.domain.model.inventory

data class Inventory(
    val productId: Int,

    val stock: Double,

    val updatedAt: Long
)