package com.litvy.litvysales.data.repository.sales

import com.litvy.litvysales.data.local.dao.sales.CashSessionScheduleDao
import com.litvy.litvysales.data.mapper.sales.toDomain
import com.litvy.litvysales.data.mapper.sales.toEntity
import com.litvy.litvysales.domain.interfaces.sales.CashSessionScheduleRepository
import com.litvy.litvysales.domain.model.sales.CashSessionSchedule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CashSessionScheduleRepositoryImpl(
    private val dao: CashSessionScheduleDao
) : CashSessionScheduleRepository {

    override fun getSchedules(): Flow<List<CashSessionSchedule>> =
        dao.getAll().map { items -> items.map { it.toDomain() } }

    override suspend fun getScheduleById(id: Int): CashSessionSchedule? =
        dao.getById(id)?.toDomain()

    override suspend fun save(schedule: CashSessionSchedule): Long {
        return if (schedule.id == 0) {
            dao.insert(schedule.toEntity())
        } else {
            dao.update(schedule.toEntity())
            schedule.id.toLong()
        }
    }

    override suspend fun delete(scheduleId: Int) {
        dao.deleteById(scheduleId)
    }
}
