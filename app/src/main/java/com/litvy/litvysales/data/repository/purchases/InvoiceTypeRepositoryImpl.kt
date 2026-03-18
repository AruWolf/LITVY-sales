package com.litvy.litvysales.data.repository.purchases

import com.litvy.litvysales.data.local.dao.purchases.InvoiceTypeDao
import com.litvy.litvysales.data.mapper.purchase.toDomain
import com.litvy.litvysales.data.mapper.purchase.toEntity
import com.litvy.litvysales.domain.interfaces.purchases.InvoiceTypeRepository
import com.litvy.litvysales.domain.model.purchases.InvoiceType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InvoiceTypeRepositoryImpl(
    private val dao: InvoiceTypeDao
): InvoiceTypeRepository {
    override suspend fun create(invoiceType: InvoiceType): Long {
        return dao.insert(invoiceType.toEntity())
    }

    override suspend fun update(invoiceType: InvoiceType) {
        dao.update(invoiceType.toEntity())
    }

    override suspend fun getById(invoiceTypeId: Int): InvoiceType? {
        return dao.getById(invoiceTypeId)?.toDomain()
    }

    override suspend fun getByCode(code: String): InvoiceType? {
        return dao.getByCode(code)?.toDomain()
    }

    override suspend fun getAll(): Flow<List<InvoiceType?>> {
        return dao.getAll().map { list -> list.map { it?.toDomain() } }
    }
}