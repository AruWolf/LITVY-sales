package com.litvy.litvysales.domain.interfaces.sales

import com.litvy.litvysales.domain.model.util.PaymentMethod

interface PaymentMethodRepository {

    suspend fun create(paymentMethod: PaymentMethod)

    suspend fun update(paymentMethod: PaymentMethod)
    suspend fun getAll(): List<PaymentMethod>

    suspend fun getById(id: Int): PaymentMethod?
    suspend fun delete(paymentMethod: PaymentMethod)

}