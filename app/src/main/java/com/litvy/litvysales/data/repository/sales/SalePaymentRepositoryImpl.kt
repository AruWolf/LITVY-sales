package com.litvy.litvysales.data.repository.sales

import com.litvy.litvysales.data.local.dao.sales.SalePaymentDao
import com.litvy.litvysales.data.mapper.sales.toDomain
import com.litvy.litvysales.data.mapper.sales.toEntity
import com.litvy.litvysales.domain.interfaces.sales.SalePaymentRepository
import com.litvy.litvysales.domain.model.sales.SalePayment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SalePaymentRepositoryImpl(private val dao: SalePaymentDao): SalePaymentRepository {
    override suspend fun create(salePayment: SalePayment): Long {
        return dao.insert(salePayment.toEntity())
    }

    override suspend fun update(salePayment: SalePayment) {
        dao.update(salePayment.toEntity())
    }

    override suspend fun getById(salePaymentId: Int): SalePayment? {
        return dao.getById(salePaymentId)?.toDomain()
    }

    override fun getBySale(saleId: Int): Flow<List<SalePayment>> {
        return dao.getBySale(saleId).map { list -> list.map { it.toDomain() } }
    }

    override fun getAll(): Flow<List<SalePayment>> {
        return dao.getAll().map {
            list -> list.map { it.toDomain()}
        }
    }

}
