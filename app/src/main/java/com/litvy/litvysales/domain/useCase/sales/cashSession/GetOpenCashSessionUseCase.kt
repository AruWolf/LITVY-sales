package com.litvy.litvysales.domain.useCase.sales.cashSession

import com.litvy.litvysales.domain.interfaces.sales.CashSessionRepository

class GetOpenCashSessionUseCase(
    private val repository: CashSessionRepository
) {
    suspend operator fun invoke(registerId: Int) =
        repository.getOpenSessionByRegister(registerId)
}