package com.litvy.litvysales.domain.filter

import com.litvy.litvysales.domain.model.enums.CashSessionStatus

data class CashSessionFilter(
    val registerId: Int? = null,
    val status: CashSessionStatus? = null,
    val openedBy: Int? = null,
    val closedBy: Int? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val isOpen: Boolean? = null
)