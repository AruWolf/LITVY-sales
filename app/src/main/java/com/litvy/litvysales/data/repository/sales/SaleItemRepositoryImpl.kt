package com.litvy.litvysales.data.repository.sales

import com.litvy.litvysales.data.local.dao.sales.SaleItemDao
import com.litvy.litvysales.data.mapper.sales.toDomain
import com.litvy.litvysales.data.mapper.sales.toEntity
import com.litvy.litvysales.domain.interfaces.sales.SaleItemRepository
import com.litvy.litvysales.domain.model.sales.SaleItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SaleItemRepositoryImpl(
    private val dao: SaleItemDao
): SaleItemRepository {
    override suspend fun create(item: SaleItem) {
        dao.insert(item.toEntity())
    }

    override fun getBySale(saleId: Int): Flow<List<SaleItem>> {
        return dao.getBySale(saleId).map { list -> list.map {it.toDomain()} }
    }
}