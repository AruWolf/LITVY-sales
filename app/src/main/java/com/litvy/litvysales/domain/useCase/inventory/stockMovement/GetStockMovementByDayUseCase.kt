package com.litvy.litvysales.domain.useCase.inventory.stockMovement

import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.model.inventory.StockMovement
import kotlinx.coroutines.flow.Flow

class GetStockMovementByDayUseCase(
    private val repository: StockMovementRepository) {
    operator fun invoke(createdAt: Long): Flow<List<StockMovement>>{
        return repository.getStockMovementByDay(createdAt)
    }
}