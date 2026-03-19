package com.litvy.litvysales.data.repository.inventory

import com.litvy.litvysales.data.local.dao.inventory.StockBatchDao
import com.litvy.litvysales.data.local.query.inventory.StockBatchQueryBuilder
import com.litvy.litvysales.data.mapper.inventory.toDomain
import com.litvy.litvysales.domain.interfaces.inventory.StockBatchRepository
import com.litvy.litvysales.domain.model.inventory.StockBatch
import com.litvy.litvysales.data.mapper.inventory.toEntity
import com.litvy.litvysales.domain.filter.inventory.StockBatchFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StockBatchRepositoryImpl(
    private val stockBatchDao: StockBatchDao
): StockBatchRepository {
    override suspend fun loadBatch(batch: StockBatch): Long {
        return stockBatchDao.loadBatch(batch.toEntity())
    }

    override suspend fun getBatch(batchId: Int): StockBatch? {
        return stockBatchDao.getFullBatch(batchId)?.toDomain()
    }

    override fun existsById(batchId: Int?): Boolean {
        return stockBatchDao.existsById(batchId)
    }

    override fun belongsToProduct(batchId: Int, productId: Int): Boolean {
        return stockBatchDao.belongsToProduct(batchId, productId)
    }

    override fun getStockBatches(filter: StockBatchFilter) =
        stockBatchDao.getByFilter(StockBatchQueryBuilder.build(filter)).map {
            list -> list.map { it.toDomain() }
        }
}
