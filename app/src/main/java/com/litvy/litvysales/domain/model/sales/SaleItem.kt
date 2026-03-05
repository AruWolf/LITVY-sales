package com.litvy.litvysales.domain.model.sales

data class SaleItem(
    val id: Int = 0,
    val saleId: Int,
    val productId: Int,
    val paymentMethodId: Int,
    val quantity: Double,
    val unitPriceInCents: Long,
    val discountAppliedInCents: Long = 0,
    val originalUnitPriceInCents: Long,
    val totalInCents: Long
)
