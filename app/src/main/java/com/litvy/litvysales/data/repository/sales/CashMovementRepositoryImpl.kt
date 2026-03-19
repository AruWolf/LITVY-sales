package com.litvy.litvysales.data.repository.sales

import com.litvy.litvysales.data.local.dao.sales.CashMovementDao
import com.litvy.litvysales.data.local.query.sales.CashMovementQueryBuilder
import com.litvy.litvysales.data.mapper.sales.toDomain
import com.litvy.litvysales.data.mapper.sales.toEntity
import com.litvy.litvysales.domain.filter.sales.CashMovementFilter
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

    override fun getCashMovements(filter: CashMovementFilter) =
        dao.getByFilter(CashMovementQueryBuilder.build(filter)).map {
            list -> list.map { it.toDomain() }
        }


}