package com.litvy.litvysales.domain.useCase.sales.cashSession

import com.litvy.litvysales.domain.interfaces.sales.CashSessionScheduleRepository

class GetCashSessionSchedulesUseCase(
    private val repository: CashSessionScheduleRepository
) {
    operator fun invoke() = repository.getSchedules()
}
