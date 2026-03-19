package com.litvy.litvysales.domain.interfaces.sales

import com.litvy.litvysales.domain.model.sales.SalePayment
import kotlinx.coroutines.flow.Flow

interface SalePaymentRepository {

    suspend fun create(salePayment: SalePayment): Long

    suspend fun update(salePayment: SalePayment)

    suspend fun getById(salePaymentId: Int): SalePayment?

    fun getAll(): Flow<List<SalePayment>>

}