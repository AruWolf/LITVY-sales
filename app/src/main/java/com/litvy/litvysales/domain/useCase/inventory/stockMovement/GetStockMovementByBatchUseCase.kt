package com.litvy.litvysales.domain.useCase.inventory.stockMovement

import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.model.inventory.StockBatch
import com.litvy.litvysales.domain.model.inventory.StockMovement
import kotlinx.coroutines.flow.Flow

class GetStockMovementByBatchUseCase(
    private val repository: StockMovementRepository
) {
    operator fun invoke(batchId: Int): Flow<List<StockMovement>>{
        return repository.getStockMovementByBatch(batchId)
    }
}