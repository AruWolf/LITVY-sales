package com.litvy.litvysales.domain.useCase.purchases.purchaseOrder

import com.litvy.litvysales.domain.interfaces.purchases.PurchaseOrderRepository
import com.litvy.litvysales.domain.model.purchases.PurchaseOrder
import com.litvy.litvysales.domain.model.purchases.PurchaseOrderItem
import com.litvy.litvysales.domain.validation.CommonValidators
import com.litvy.litvysales.domain.validation.ValidationBuilder
import com.litvy.litvysales.domain.validation.ValidationResult

class CreatePurchaseOrderUseCase(
    private val repository: PurchaseOrderRepository
) {

    suspend operator fun invoke(
        purchaseOrder: PurchaseOrder,
        items: List<PurchaseOrderItem>
    ): ValidationResult {
        val validator = ValidationBuilder()

        validator.check(
            purchaseOrder.providerId > 0,
            "providerId",
            "Debe seleccionarse un proveedor valido"
        )
        validator.add(
            CommonValidators.positive(
                field = "createdBy",
                value = purchaseOrder.createdBy.toLong()
            )
        )
        validator.check(
            items.isNotEmpty(),
            "items",
            "La orden de compra debe incluir al menos un item"
        )

        val result = validator.build()
        if (result is ValidationResult.Failure && result.errors.isNotEmpty()) {
            return result
        }

        repository.create(purchaseOrder, items)
        return ValidationResult.Success
    }
}
