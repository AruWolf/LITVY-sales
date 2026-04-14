package com.litvy.litvysales.domain.model.sales

data class SaleDetail(
    val sale: Sale,
    val items: List<SaleDetailItem>,
    val payments: List<SalePayment>
)

data class SaleDetailItem(
    val id: Int,
    val productId: Int,
    val productName: String,
    val quantity: Double,
    val totalInCents: Long
)
