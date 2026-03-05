package com.litvy.litvysales.domain.useCase.inventory.stockMovement

import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.model.inventory.StockMovement

class GetStockMovementUseCase(
    private val repository: StockMovementRepository
) {
    suspend fun invoke(stockMovementId: Int): StockMovement?{
        return repository.getStockMovement(stockMovementId)
    }
}