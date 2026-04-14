package com.litvy.litvysales.domain.model.sales

data class CashSessionDetail(
    val session: CashSession,
    val totalSoldInCents: Long,
    val expectedAmountInCents: Long,
    val sales: List<CashSessionSaleSummary>,
    val movements: List<CashMovement>
)
