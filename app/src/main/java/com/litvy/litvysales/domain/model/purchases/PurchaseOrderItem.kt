package com.litvy.litvysales.domain.model.purchases

data class PurchaseOrderItem(
    val id: Int = 0,

    val purchaseOrderId: Int,
    val productId: Int,

    val quantity: Double
)
