package com.litvy.litvysales.domain.useCase.sales.paymentmethod

import com.litvy.litvysales.domain.interfaces.sales.PaymentMethodRepository
import com.litvy.litvysales.domain.model.util.PaymentMethod

class DeletePaymentMethodUseCase(
    private val repository: PaymentMethodRepository
) {

    suspend operator fun invoke(paymentMethod: PaymentMethod){
        repository.delete(paymentMethod)
    }
}