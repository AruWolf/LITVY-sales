package com.litvy.litvysales.domain.model.sales

data class CashMovement(
    val id: Int = 0,

    val cashSessionId: Int,

    val type: String,

    val amountInCents: Long,

    val reason: String?,

    val createdAt: Long,

    val createdBy: Int
)