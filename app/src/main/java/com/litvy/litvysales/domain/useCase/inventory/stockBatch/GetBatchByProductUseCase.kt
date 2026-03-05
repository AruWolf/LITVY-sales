package com.litvy.litvysales.domain.useCase.inventory.stockBatch

import com.litvy.litvysales.domain.interfaces.inventory.StockBatchRepository
import com.litvy.litvysales.domain.model.inventory.StockBatch
import kotlinx.coroutines.flow.Flow

class GetBatchByProductUseCase(
    private val repository: StockBatchRepository
) {
    operator fun invoke(productId: Int): Flow<List<StockBatch>> {
        return repository.getBatchByProduct(productId)
    }
}