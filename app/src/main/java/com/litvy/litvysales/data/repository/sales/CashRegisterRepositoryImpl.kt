package com.litvy.litvysales.data.repository.sales

import com.litvy.litvysales.data.local.dao.sales.CashRegisterDao
import com.litvy.litvysales.data.mapper.sales.toDomain
import com.litvy.litvysales.data.mapper.sales.toEntity
import com.litvy.litvysales.domain.interfaces.sales.CashRegisterRepository
import com.litvy.litvysales.domain.model.sales.CashRegister
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CashRegisterRepositoryImpl(
    private val dao: CashRegisterDao
): CashRegisterRepository {
    override suspend fun create(cashRegister: CashRegister): Long {
        return dao.insert(cashRegister.toEntity())
    }

    override suspend fun toggleActive(cashRegisterId: Int) {
        dao.toggleActive(cashRegisterId)
    }

    override fun getAll(): Flow<List<CashRegister?>> {
        return dao.getAll().map { list -> list.map { it?.toDomain() } }
    }

    override fun getAllActives(): Flow<List<CashRegister?>> {
        return dao.getActives().map { list -> list.map { it?.toDomain() } }
    }

    override suspend fun getById(cashRegisterId: Int): CashRegister? {
        return dao.getById(cashRegisterId)?.toDomain()
    }

    override suspend fun getByName(name: String): CashRegister? {
        return dao.getByName(name)?.toDomain()
    }

    override fun getByLocation(location: String): Flow<List<CashRegister?>> {
        return dao.getByLocation(location).map { list -> list.map { it?.toDomain() } }
    }
}