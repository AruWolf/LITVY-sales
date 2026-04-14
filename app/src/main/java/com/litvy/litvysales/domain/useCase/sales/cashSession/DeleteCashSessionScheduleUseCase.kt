package com.litvy.litvysales.domain.useCase.sales.cashSession

import com.litvy.litvysales.domain.interfaces.sales.CashSessionScheduleRepository

class DeleteCashSessionScheduleUseCase(
    private val repository: CashSessionScheduleRepository
) {
    suspend operator fun invoke(scheduleId: Int) {
        repository.delete(scheduleId)
    }
}
