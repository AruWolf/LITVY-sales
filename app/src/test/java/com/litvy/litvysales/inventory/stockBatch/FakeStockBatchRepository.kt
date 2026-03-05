package com.litvy.litvysales.inventory.stockBatch

import com.litvy.litvysales.domain.interfaces.inventory.StockBatchRepository
import com.litvy.litvysales.domain.model.inventory.StockBatch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeStockBatchRepository : StockBatchRepository {

    private val batches = mutableListOf<StockBatch>()

    override suspend fun loadBatch(batch: StockBatch) {
        batches.add(batch)
    }

    override suspend fun getBatch(batchId: Int): StockBatch? {
        return batches.find { it.id == batchId }
    }

    override fun getBatchByProduct(productId: Int): Flow<List<StockBatch>> {
        return flowOf(batches.filter { it.productId == productId })
    }
}