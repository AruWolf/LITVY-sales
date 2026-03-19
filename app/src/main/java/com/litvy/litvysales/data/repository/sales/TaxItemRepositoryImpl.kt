package com.litvy.litvysales.data.repository.sales

import com.litvy.litvysales.data.local.dao.sales.TaxItemDao
import com.litvy.litvysales.data.mapper.sales.toDomain
import com.litvy.litvysales.data.mapper.sales.toEntity
import com.litvy.litvysales.domain.interfaces.sales.TaxItemRepository
import com.litvy.litvysales.domain.model.sales.TaxItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaxItemRepositoryImpl(
    private val dao: TaxItemDao
): TaxItemRepository {
    override suspend fun create(taxItem: TaxItem): Long {
        return dao.insert(taxItem.toEntity())
    }

    override suspend fun update(taxItem: TaxItem) {
        return dao.update(taxItem.toEntity())
    }

    override suspend fun getById(taxItemId: Int): TaxItem? {
        return dao.getById(taxItemId)?.toDomain()
    }

    override fun getAll(): Flow<List<TaxItem?>> {
        return dao.getAll().map { 
            list -> list.map { it?.toDomain() }
        }
    }

}