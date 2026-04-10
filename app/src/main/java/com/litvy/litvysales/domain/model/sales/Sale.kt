package com.litvy.litvysales.domain.model.sales

data class Sale(
    val id: Int = 0,

    val cashSessionId: Int,
    val sellerId: Int,
    val customerId: Int?,
    val totalDiscountInCents: Long,
    val totalInCents: Long,
    val status: com.litvy.litvysales.domain.model.enums.SaleStatus,
    val cancellationReason: String?,
    val createdAt: Long
)
