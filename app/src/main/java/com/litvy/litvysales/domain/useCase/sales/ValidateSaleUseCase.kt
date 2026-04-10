package com.litvy.litvysales.domain.useCase.sales

import com.litvy.litvysales.domain.model.sales.Sale
import com.litvy.litvysales.domain.model.sales.SaleItem
import com.litvy.litvysales.domain.model.sales.SalePayment
import com.litvy.litvysales.domain.validation.ValidationBuilder
import com.litvy.litvysales.domain.validation.ValidationResult

class ValidateSaleUseCase {

    operator fun invoke(
        sale: Sale,
        items: List<SaleItem>,
        payments: List<SalePayment>
    ): ValidationResult {

        val validator = ValidationBuilder()

        validator.check(
            items.isNotEmpty(),
            "items",
            "Debe agregar al menos un producto"
        )

        validator.check(
            payments.isNotEmpty(),
            "payments",
            "Debe agregar al menos un pago"
        )

        val totalPayments = payments.sumOf { it.amountInCents }

        validator.check(
            totalPayments == sale.totalInCents,
            "payments",
            "Los pagos no coinciden con el total"
        )

        return validator.build()
    }
}