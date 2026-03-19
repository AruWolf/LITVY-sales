package com.litvy.litvysales.domain.useCase.inventory.stockBatch

import com.litvy.litvysales.domain.filter.inventory.StockBatchFilter
import com.litvy.litvysales.domain.interfaces.inventory.StockBatchRepository
import com.litvy.litvysales.domain.model.inventory.StockBatch
import kotlinx.coroutines.flow.Flow

class GetStockBatchUseCase(
    private val repository: StockBatchRepository
) {

    suspend operator fun invoke(filter: StockBatchFilter): Flow<List<StockBatch?>>{
        return repository.getStockBatches(filter)
    }
}