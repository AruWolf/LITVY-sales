package com.litvy.litvysales.domain.useCase.inventory.stockBatch

import com.litvy.litvysales.domain.interfaces.inventory.StockBatchRepository
import com.litvy.litvysales.domain.model.inventory.StockBatch

class GetStockBatchIdUseCase(
    private val repository: StockBatchRepository
) {
    suspend fun invoke(batchId: Int): StockBatch?{
        return repository.getBatch(batchId)
    }
}