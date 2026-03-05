package com.litvy.litvysales.domain.model.sales

import com.litvy.litvysales.data.local.entity.enums.SaleStatus

data class Sale(
    val id: Int = 0,

    val cashSessionId: Int,
    val sellerId: Int,
    val customerId: Int?,
    val totalDiscountInCents: Long,
    val totalInCents: Long,
    val status: SaleStatus,
    val cancellationReason: String?,
    val createdAt: Long
)
