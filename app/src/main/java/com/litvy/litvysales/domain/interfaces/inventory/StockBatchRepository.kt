package com.litvy.litvysales.domain.interfaces.inventory

import com.litvy.litvysales.domain.filter.inventory.StockBatchFilter
import com.litvy.litvysales.domain.model.inventory.StockBatch
import kotlinx.coroutines.flow.Flow

interface StockBatchRepository {
    suspend fun loadBatch(batch: StockBatch)
    suspend fun getBatch(batchId: Int): StockBatch?
    fun existsById(batchId: Int?): Boolean
    fun belongsToProduct(batchId: Int, productId:Int): Boolean

    fun getStockBatches(filter: StockBatchFilter): Flow<List<StockBatch?>>
}