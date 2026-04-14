package com.litvy.litvysales.domain.model.sales

data class CashSessionSaleSummary(
    val saleId: Int,
    val createdAt: Long,
    val itemCount: Int,
    val totalInCents: Long
)
