package com.litvy.litvysales.data.repository.util

import com.litvy.litvysales.data.local.dao.util.PaymentMethodDao
import com.litvy.litvysales.data.mapper.util.toDomain
import com.litvy.litvysales.data.mapper.util.toEntity
import com.litvy.litvysales.domain.interfaces.sales.PaymentMethodRepository
import com.litvy.litvysales.domain.model.util.PaymentMethod
import kotlinx.coroutines.flow.first

class PaymentMethodRepositoryImpl(
    private val dao: PaymentMethodDao
) : PaymentMethodRepository {

    override suspend fun create(paymentMethod: PaymentMethod){
        return dao.insert(paymentMethod.toEntity())
    }

    override suspend fun update(paymentMethod: PaymentMethod) {
        dao.update(paymentMethod.toEntity())
    }

    override suspend fun delete(paymentMethod: PaymentMethod) {
        dao.delete(paymentMethod.toEntity())
    }

    override suspend fun getAll(): List<PaymentMethod> {
        return dao.getAll().first().mapNotNull { it?.toDomain() }
    }

    override suspend fun getById(id: Int): PaymentMethod? {
        return dao.getById(id)?.toDomain()
    }
}
