package com.litvy.litvysales.domain.useCase.inventory.stockMovement

import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.model.inventory.StockMovement
import kotlinx.coroutines.flow.Flow

class GetStockMovementByProductUseCase(
    private val repository: StockMovementRepository
) {
    operator fun invoke(productId: Int): Flow<List<StockMovement>> {
        return repository.getStockMovementByProduct(productId)
    }
}