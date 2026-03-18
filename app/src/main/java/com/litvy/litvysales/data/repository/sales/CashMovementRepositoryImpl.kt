package com.litvy.litvysales.data.repository.sales

import com.litvy.litvysales.data.local.dao.sales.CashMovementDao
import com.litvy.litvysales.data.mapper.sales.toDomain
import com.litvy.litvysales.data.mapper.sales.toEntity
import com.litvy.litvysales.domain.interfaces.sales.CashMovementRepository
import com.litvy.litvysales.domain.model.sales.CashMovement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CashMovementRepositoryImpl(
    private val dao: CashMovementDao
): CashMovementRepository {
    override suspend fun create(movement: CashMovement): Long {
        return dao.load(movement.toEntity())
    }

    override suspend fun getById(id: Int): CashMovement? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun getBySession(sessionId: Int): Flow<List<CashMovement?>> {
        return dao.getByCashSession(sessionId).map { list -> list.map { it?.toDomain() } }
    }

    override suspend fun getByType(type: String): Flow<List<CashMovement?>> {
        return dao.getByType(type).map { list -> list.map { it?.toDomain() } }
    }

    override suspend fun getByCreationDate(createdAt: Long): Flow<List<CashMovement?>> {
        return dao.getByCreationDate(createdAt).map { list -> list.map { it?.toDomain() } }
    }

    override suspend fun getByUser(userId: Int): Flow<List<CashMovement?>> {
        return dao.getByUser(userId).map { list -> list.map { it?.toDomain() } }
    }
}