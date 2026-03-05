package com.litvy.litvysales.data.repository.inventory

import com.litvy.litvysales.data.local.dao.inventory.StockBatchDao
import com.litvy.litvysales.data.mapper.inventory.toDomain
import com.litvy.litvysales.domain.interfaces.inventory.StockBatchRepository
import com.litvy.litvysales.domain.model.inventory.StockBatch
import com.litvy.litvysales.data.mapper.inventory.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StockBatchRepository(
    private val stockBatchDao: StockBatchDao
): StockBatchRepository {
    override suspend fun loadBatch(batch: StockBatch) {
        stockBatchDao.loadBatch(batch.toEntity())
    }

    override suspend fun getBatch(batchId: Int): StockBatch? {
        return stockBatchDao.getFullBatch(batchId)?.toDomain()
    }

    override fun getBatchByProduct(productId: Int): Flow<List<StockBatch>> {
        return stockBatchDao.getBatchByProduct(productId).map {list -> list.map {it.toDomain()}}
    }
}