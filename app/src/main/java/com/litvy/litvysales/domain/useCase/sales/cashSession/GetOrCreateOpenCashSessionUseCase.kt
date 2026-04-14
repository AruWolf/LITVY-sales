package com.litvy.litvysales.domain.useCase.sales.cashSession

import com.litvy.litvysales.domain.interfaces.sales.CashSessionScheduleRepository
import com.litvy.litvysales.domain.model.sales.CashSession
import com.litvy.litvysales.domain.model.sales.CashSessionDefaults
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.ZoneId

class GetOrCreateOpenCashSessionUseCase(
    private val getOpenCashSessionUseCase: GetOpenCashSessionUseCase,
    private val openCashSessionUseCase: OpenCashSessionUseCase,
    private val scheduleRepository: CashSessionScheduleRepository
) {

    suspend operator fun invoke(
        registerId: Int = CashSessionDefaults.DEFAULT_CASH_REGISTER_ID,
        openedBy: Int = CashSessionDefaults.DEFAULT_USER_ID,
        now: Long = System.currentTimeMillis()
    ): CashSession {
        val openSession = getOpenCashSessionUseCase(registerId)
        if (openSession != null) {
            return openSession
        }

        val schedules = scheduleRepository.getSchedules().first()
        val zoneId = ZoneId.systemDefault()
        val localDateTime = Instant.ofEpochMilli(now).atZone(zoneId).toLocalDateTime()
        val dayOfWeek = localDateTime.dayOfWeek.value
        val minuteOfDay = localDateTime.hour * 60 + localDateTime.minute

        val scheduledWindow = schedules.firstOrNull { schedule ->
            schedule.active &&
                schedule.dayOfWeek == dayOfWeek &&
                minuteOfDay in schedule.openMinuteOfDay..schedule.closeMinuteOfDay
        }

        val openingAmount = if (scheduledWindow != null) 0L else 0L

        return openCashSessionUseCase(
            registerId = registerId,
            openedBy = openedBy,
            openingAmountInCents = openingAmount,
            startedAt = now
        )
    }
}
