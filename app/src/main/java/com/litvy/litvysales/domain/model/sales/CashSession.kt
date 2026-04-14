package com.litvy.litvysales.domain.model.sales

import com.litvy.litvysales.domain.model.enums.CashSessionStatus

data class CashSession(
    val id: Int = 0,
    val cashRegisterId: Int,
    val startedAt: Long,
    val closedAt: Long?,

    val openingAmountInCents: Long,
    val closingAmountInCents: Long?,
    val expectedAmountInCents: Long?,
    val differenceInCents: Long?,
    val differenceJustification: String?,

    val status: CashSessionStatus,
    val openedBy: Int,
    val closedBy: Int?
)
