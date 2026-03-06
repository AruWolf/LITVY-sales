package com.litvy.litvysales.domain.interfaces.sales

import com.litvy.litvysales.domain.model.sales.Sale
import kotlinx.coroutines.flow.Flow

interface SaleRepository {

    suspend fun create(sale: Sale): Long

    suspend fun update(sale: Sale)

    suspend fun getById(id: Int): Sale?

    fun getBySession(sessionId: Int): Flow<List<Sale>>

    suspend fun getByPeriod(startDate: Long, endDate: Long): List<Sale>

    suspend fun cancelSale(
        saleId: Int,
        reason: String
    )

}