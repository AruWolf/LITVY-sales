package com.litvy.litvysales.domain.model.sales

import com.litvy.litvysales.domain.model.enums.CashSessionStatus

data class CashSessionSummary(
    val sessionId: Int,
    val startedAt: Long,
    val closedAt: Long?,
    val openedByLabel: String,
    val totalSoldInCents: Long,
    val status: CashSessionStatus
)
