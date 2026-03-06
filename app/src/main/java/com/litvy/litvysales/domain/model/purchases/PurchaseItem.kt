package com.litvy.litvysales.domain.model.purchases

data class PurchaseItem(
    val id: Int = 0,

    val purchaseId: Int,

    val productId: Int,

    val quantity: Double,

    val unitPriceInCents: Long
)