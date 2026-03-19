package com.litvy.litvysales.data.repository.inventory

import com.litvy.litvysales.data.local.dao.inventory.StockMovementDao
import com.litvy.litvysales.data.local.query.inventory.StockMovementQueryBuilder
import com.litvy.litvysales.data.mapper.inventory.toDomain
import com.litvy.litvysales.data.mapper.inventory.toEntity
import com.litvy.litvysales.domain.filter.inventory.StockMovementFilter
import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.model.inventory.StockMovement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StockMovementRepositoryImpl(
    private val stockMovementDao: StockMovementDao
): StockMovementRepository {

    override suspend fun generateMovement(stockMovement: StockMovement) {
        stockMovementDao.generateMovement(stockMovement.toEntity())
    }

    override suspend fun getStockMovement(stockMovementId: Int): StockMovement? {
        return stockMovementDao.getStockMovementById(stockMovementId)?.toDomain()
    }

    override fun getAllStockMovement(): Flow<List<StockMovement>> {
        return stockMovementDao.getAllStockMovement().map {list -> list.map {it.toDomain()}}
    }

    override fun getStockMovements(filter: StockMovementFilter)=
        stockMovementDao.getByFilter(StockMovementQueryBuilder.build(filter)).map {
            list -> list.map { it?.toDomain() }
        }
}