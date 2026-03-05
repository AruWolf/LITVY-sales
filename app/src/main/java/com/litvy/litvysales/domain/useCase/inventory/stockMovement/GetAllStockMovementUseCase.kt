package com.litvy.litvysales.domain.useCase.inventory.stockMovement

import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.model.inventory.StockMovement
import kotlinx.coroutines.flow.Flow

class GetAllStockMovementUseCase(
    private val repository: StockMovementRepository
) {
    operator fun invoke(): Flow<List<StockMovement>> {
        return repository.getAllStockMovement()
    }
}