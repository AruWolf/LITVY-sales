package com.litvy.litvysales.data.repository.sales

import com.litvy.litvysales.data.local.dao.sales.SaleDao
import com.litvy.litvysales.data.local.query.sales.SaleQueryBuilder
import com.litvy.litvysales.domain.interfaces.sales.SaleRepository
import com.litvy.litvysales.domain.model.sales.*
import com.litvy.litvysales.data.mapper.sales.toEntity
import com.litvy.litvysales.data.mapper.sales.toDomain
import com.litvy.litvysales.domain.filter.sales.SaleFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

    class SaleRepositoryImpl(private val dao: SaleDao): SaleRepository {

        override suspend fun create(sale: Sale): Long {
            return dao.insert(sale.toEntity())
        }

        override suspend fun update(sale: Sale) {
            dao.update(sale.toEntity())
        }

        override suspend fun getById(id: Int): Sale? {
            return dao.getById(id)?.toDomain()
        }

        override suspend fun getByPeriod(
            startDate: Long,
            endDate: Long
        ): List<Sale> {
            TODO("Not yet implemented")
        }

        override suspend fun cancelSale(saleId: Int, reason: String) {
            TODO("Not yet implemented")
        }

        override fun getAll(): Flow<List<Sale?>> {
            return dao.getAll().map { list -> list.map { it?.toDomain() } }
        }

        override fun getSales(filter: SaleFilter) =
            dao.getByFilter(SaleQueryBuilder.build(filter)).map {
                list -> list.map { it.toDomain() }
            }
}