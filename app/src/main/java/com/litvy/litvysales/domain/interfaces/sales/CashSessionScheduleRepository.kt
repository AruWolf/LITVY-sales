package com.litvy.litvysales.domain.interfaces.sales

import com.litvy.litvysales.domain.model.sales.CashSessionSchedule
import kotlinx.coroutines.flow.Flow

interface CashSessionScheduleRepository {
    fun getSchedules(): Flow<List<CashSessionSchedule>>

    suspend fun getScheduleById(id: Int): CashSessionSchedule?

    suspend fun save(schedule: CashSessionSchedule): Long

    suspend fun delete(scheduleId: Int)
}
