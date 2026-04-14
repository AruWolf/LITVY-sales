package com.litvy.litvysales.domain.useCase.sales.cashSession

import com.litvy.litvysales.domain.interfaces.sales.CashSessionScheduleRepository
import com.litvy.litvysales.domain.model.sales.CashSessionSchedule

class SaveCashSessionScheduleUseCase(
    private val repository: CashSessionScheduleRepository
) {

    suspend operator fun invoke(schedule: CashSessionSchedule): Long {
        if (schedule.dayOfWeek !in 1..7) {
            throw IllegalArgumentException("Día inválido.")
        }
        if (schedule.openMinuteOfDay !in 0..1439 || schedule.closeMinuteOfDay !in 0..1439) {
            throw IllegalArgumentException("Horario inválido.")
        }
        if (schedule.closeMinuteOfDay <= schedule.openMinuteOfDay) {
            throw IllegalArgumentException("La hora de cierre debe ser posterior a la apertura.")
        }

        return repository.save(
            schedule.copy(
                title = schedule.title?.trim().takeUnless { it.isNullOrBlank() }
            )
        )
    }
}
