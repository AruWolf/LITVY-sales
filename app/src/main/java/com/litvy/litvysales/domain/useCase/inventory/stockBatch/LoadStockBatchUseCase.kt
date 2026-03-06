package com.litvy.litvysales.domain.useCase.inventory.stockBatch

import com.litvy.litvysales.domain.interfaces.inventory.StockBatchRepository
import com.litvy.litvysales.domain.model.inventory.StockBatch

class LoadStockBatchUseCase(
    private val stockBatchRepository: StockBatchRepository
) {

    suspend operator fun invoke(batch: StockBatch) {

        if (batch.quantity <= 0) {
            throw IllegalArgumentException("Batch quantity must be greater than 0")
        }

        stockBatchRepository.loadBatch(batch)
    }

}