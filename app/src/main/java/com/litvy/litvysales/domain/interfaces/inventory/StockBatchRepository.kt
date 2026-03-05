package com.litvy.litvysales.domain.interfaces.inventory

import com.litvy.litvysales.domain.model.inventory.StockBatch
import kotlinx.coroutines.flow.Flow

interface StockBatchRepository {
    suspend fun loadBatch(batch: StockBatch)
    suspend fun getBatch(batchId: Int): StockBatch?
    fun getBatchByProduct(productId: Int): Flow<List<StockBatch>>
}