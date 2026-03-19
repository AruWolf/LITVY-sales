package com.litvy.litvysales.domain.interfaces.inventory

import com.litvy.litvysales.domain.filter.inventory.StockMovementFilter
import com.litvy.litvysales.domain.model.enums.StockMovementType
import com.litvy.litvysales.domain.model.inventory.StockMovement
import kotlinx.coroutines.flow.Flow

interface StockMovementRepository {

    suspend fun generateMovement(stockMovement: StockMovement)
    suspend fun getStockMovement(stockMovementId: Int): StockMovement?
    fun getAllStockMovement(): Flow<List<StockMovement>>
    fun getStockMovements(filter: StockMovementFilter): Flow<List<StockMovement?>>
}