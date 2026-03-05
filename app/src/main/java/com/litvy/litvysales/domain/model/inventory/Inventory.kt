package com.litvy.litvysales.domain.model.inventory

data class Inventory(
    val productId: Long,

    val stock: Double,

    val updatedAt: Long
)