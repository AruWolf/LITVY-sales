package com.litvy.litvysales.domain.useCase.sales

import com.litvy.litvysales.domain.interfaces.sales.PaymentMethodRepository
import com.litvy.litvysales.domain.model.util.PaymentMethod

class GetPaymentMethodsUseCase(
    private val repository: PaymentMethodRepository
) {

    suspend operator fun invoke(): List<PaymentMethod> {
        return repository.getAll()
    }
}
