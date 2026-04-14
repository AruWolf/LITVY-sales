package com.litvy.litvysales.domain.useCase.sales.cashSession

import com.litvy.litvysales.domain.interfaces.sales.CashSessionRepository

class GetCashSessionByIdUseCase(
    private val repository: CashSessionRepository
) {
    suspend operator fun invoke(id: Int) = repository.getById(id)
}
