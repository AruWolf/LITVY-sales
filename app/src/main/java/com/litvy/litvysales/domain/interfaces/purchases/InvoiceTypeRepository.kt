package com.litvy.litvysales.domain.interfaces.purchases

import com.litvy.litvysales.domain.model.purchases.InvoiceType
import kotlinx.coroutines.flow.Flow

interface InvoiceTypeRepository {

    suspend fun create(invoiceType: InvoiceType): Long

    suspend fun update(invoiceType: InvoiceType)

    suspend fun getById(invoiceTypeId: Int): InvoiceType?

    suspend fun getByCode(code: String): InvoiceType?

    suspend fun getAll(): Flow<List<InvoiceType?>>
}