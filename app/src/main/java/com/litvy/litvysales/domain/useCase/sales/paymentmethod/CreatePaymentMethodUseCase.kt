package com.litvy.litvysales.domain.useCase.sales.paymentmethod

import com.litvy.litvysales.domain.interfaces.sales.PaymentMethodRepository
import com.litvy.litvysales.domain.model.util.PaymentMethod
import com.litvy.litvysales.domain.validation.CommonValidators
import com.litvy.litvysales.domain.validation.ValidationBuilder
import com.litvy.litvysales.domain.validation.ValidationResult

class CreatePaymentMethodUseCase(
    private val repository: PaymentMethodRepository
) {

    suspend operator fun invoke(
        paymentMethod: PaymentMethod
    ): ValidationResult {
        val validator = ValidationBuilder()
        val cleanName = paymentMethod.name.trim()

        val existing = repository.getAll()

        val exists = existing.any(){
            cleanName.equals(it.name.trim(), ignoreCase = true)
        }

            validator.check(
                !exists,
                "name",
                "Metodo de pago ya existente"
            )

        validator.add(CommonValidators.notBlank("name", cleanName))

        validator.check(
            paymentMethod.surchargePercentage >= 0,
            "surchargePercentage",
            "Valor de recargo invalido"
        )

        val result = validator.build()

        if (result is ValidationResult.Failure && result.errors.isNotEmpty()){
            return result
        }

        repository.create(
            PaymentMethod(
                id = paymentMethod.id,
                name = cleanName,
                surchargePercentage = paymentMethod.surchargePercentage
            )
        )

        return result
    }
}