package com.litvy.litvysales.ui.sales.model

data class SaleItemDraft(
    val productId: Int,
    val name: String,
    val quantity: Double,
    val unitPrice: Long,
    val total: Long
)