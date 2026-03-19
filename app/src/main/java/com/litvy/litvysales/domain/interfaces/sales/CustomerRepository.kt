package com.litvy.litvysales.domain.interfaces.sales

import com.litvy.litvysales.domain.filter.sales.CustomerFilter
import com.litvy.litvysales.domain.model.sales.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {

    suspend fun create(customer: Customer): Long

    suspend fun update(customer: Customer)

    suspend fun getById(customerId: Int): Customer?

    fun getAll(): Flow<List<Customer?>>

    fun getCustomers(filter: CustomerFilter): Flow<List<Customer>>
}