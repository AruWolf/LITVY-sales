package com.litvy.litvysales.data.repository.util

import com.litvy.litvysales.data.local.dao.util.PaymentMethoDao
import com.litvy.litvysales.data.mapper.util.toDomain
import com.litvy.litvysales.domain.interfaces.sales.PaymentMethodRepository
import com.litvy.litvysales.domain.model.util.PaymentMethod
import kotlinx.coroutines.flow.first

class PaymentMethodRepositoryImpl(
    private val dao: PaymentMethoDao
) : PaymentMethodRepository {

    override suspend fun getAll(): List<PaymentMethod> {
        return dao.getAll().first().mapNotNull { it?.toDomain() }
    }

    override suspend fun getById(id: Int): PaymentMethod? {
        return dao.getById(id)?.toDomain()
    }
}
