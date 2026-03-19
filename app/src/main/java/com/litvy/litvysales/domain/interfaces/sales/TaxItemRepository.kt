package com.litvy.litvysales.domain.interfaces.sales

import com.litvy.litvysales.domain.model.sales.TaxItem
import kotlinx.coroutines.flow.Flow

interface TaxItemRepository {

    suspend fun create(taxItem: TaxItem): Long

    suspend fun update(taxItem: TaxItem)

    suspend fun getById(taxItemId: Int): TaxItem?

    fun getAll(): Flow<List<TaxItem?>>
}