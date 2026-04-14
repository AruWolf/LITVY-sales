package com.litvy.litvysales.domain.useCase.sales.cashSession

import com.litvy.litvysales.domain.interfaces.sales.CashSessionRepository
import com.litvy.litvysales.domain.model.sales.CashSession

class UpdateCashSessionUseCase(
    private val repository: CashSessionRepository
) {
    suspend operator fun invoke(session: CashSession) {
        repository.update(session)
    }
}
