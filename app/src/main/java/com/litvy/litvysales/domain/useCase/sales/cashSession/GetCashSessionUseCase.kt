package com.litvy.litvysales.domain.useCase.sales.cashSession

import com.litvy.litvysales.domain.filter.sales.CashSessionFilter
import com.litvy.litvysales.domain.interfaces.sales.CashSessionRepository

class GetCashSessionUseCase(
    private val repository: CashSessionRepository
) {
    operator fun invoke(filter: CashSessionFilter) =
        repository.getSessions(filter)
}