package com.litvy.litvysales.domain.interfaces.sales

import com.litvy.litvysales.domain.model.sales.SaleItem
import kotlinx.coroutines.flow.Flow

interface SaleItemRepository {

    suspend fun create(item: SaleItem)

    fun getBySale(saleId: Int): Flow<List<SaleItem>>
}