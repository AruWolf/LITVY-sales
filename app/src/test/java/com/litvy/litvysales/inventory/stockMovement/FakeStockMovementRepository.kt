package com.litvy.litvysales.inventory.stockMovement

import com.litvy.litvysales.domain.model.enums.StockMovementType
import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.model.inventory.StockMovement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeStockMovementRepository : StockMovementRepository {

    private val movements = mutableListOf<StockMovement>()

    override suspend fun generateMovement(stockMovement: StockMovement) {
        movements.add(stockMovement)
    }

    override suspend fun getStockMovement(stockMovementId: Int): StockMovement? {
        return movements.find { it.id == stockMovementId }
    }

    override fun getStockMovementByProduct(productId: Int): Flow<List<StockMovement>> {
        return flowOf(movements.filter { it.productId == productId })
    }

    override fun getStockMovementByBatch(batchId: Int): Flow<List<StockMovement>> {
        return flowOf(movements.filter { it.batchId == batchId })
    }

    override fun getStockMovementByType(type: StockMovementType): Flow<List<StockMovement>> {
        return flowOf(movements.filter { it.type == type })
    }

    override fun getStockMovementByDay(createdAt: Long): Flow<List<StockMovement>> {
        return flowOf(movements.filter { it.createdAt == createdAt })
    }

    override fun getStockMovementByReference(referenceId: Int): Flow<List<StockMovement>> {
        return flowOf(movements.filter { it.referenceId == referenceId })
    }

    override fun getStockMovementByUser(userId: Int): Flow<List<StockMovement>> {
        return flowOf(movements.filter { it.createdBy == userId })
    }

    override fun getAllStockMovement(): Flow<List<StockMovement>> {
        return flowOf(movements)
    }
}