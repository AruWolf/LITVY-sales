package com.litvy.litvysales.data.repository.sales

import com.litvy.litvysales.data.local.dao.sales.CustomerDao
import com.litvy.litvysales.data.local.query.sales.CustomerQueryBuilder
import com.litvy.litvysales.data.mapper.sales.toDomain
import com.litvy.litvysales.data.mapper.sales.toEntity
import com.litvy.litvysales.domain.filter.sales.CustomerFilter
import com.litvy.litvysales.domain.interfaces.sales.CustomerRepository
import com.litvy.litvysales.domain.model.sales.Customer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CustomerRepositoryImpl(private val dao: CustomerDao): CustomerRepository {
    override suspend fun create(customer: Customer): Long {
        return dao.insert(customer.toEntity())
    }

    override suspend fun update(customer: Customer) {
        dao.update(customer.toEntity())
    }

    override suspend fun getById(customerId: Int): Customer? {
        return dao.getById(customerId)?.toDomain()
    }

    override fun getAll(): Flow<List<Customer?>> {
        return dao.getAll().map {
            list -> list.map { it?.toDomain() }
        }
    }

    override fun getCustomers(filter: CustomerFilter) =
        dao.getByFilter(CustomerQueryBuilder.build(filter)).map {
            list -> list.map { it.toDomain() }
        }


}