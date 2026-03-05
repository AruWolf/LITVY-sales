package com.litvy.litvysales.domain.model.inventory

data class StockBatch(
    val id: Int? = 0,

    val productId: Int,

    val quantity: Double,

    val expirationDate: Long?,

    val purchaseItemId: Int?,

    val createdAt: Long
)