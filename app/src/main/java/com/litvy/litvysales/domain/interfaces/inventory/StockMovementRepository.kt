package com.litvy.litvysales.domain.interfaces.inventory

import com.litvy.litvysales.domain.model.enums.StockMovementType
import com.litvy.litvysales.domain.model.inventory.StockMovement
import kotlinx.coroutines.flow.Flow

interface StockMovementRepository {

    suspend fun generateMovement(stockMovement: StockMovement)
    suspend fun getStockMovement(stockMovementId: Int): StockMovement?
    fun getStockMovementByProduct(productId: Int): Flow<List<StockMovement>>
    fun getStockMovementByBatch(batchId: Int): Flow<List<StockMovement>>
    fun getStockMovementByType(type: StockMovementType): Flow<List<StockMovement>>
    fun getStockMovementByDay(createdAt: Long): Flow<List<StockMovement>>
    fun getStockMovementByReference(referenceId: Int): Flow<List<StockMovement>>
    fun getStockMovementByUser(userId: Int): Flow<List<StockMovement>>
    fun getAllStockMovement(): Flow<List<StockMovement>>
}