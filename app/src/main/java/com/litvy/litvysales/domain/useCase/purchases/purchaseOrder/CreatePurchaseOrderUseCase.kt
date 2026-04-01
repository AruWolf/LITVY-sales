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
            "Debe seleccionar un proveedor"
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

        items.forEachIndexed { index, item ->
            validator.check(item.productId > 0,
            "items[$index].product",
            "Debe seleccionar un producto"
            )

            validator.check(item.quantity > 0,
                "items[$index].quantity",
                "Ingrese una cantidad valida"
                )
        }

        val result = validator.build()
        if (result is ValidationResult.Failure && result.errors.isNotEmpty()) {
            return result
        }

        repository.create(purchaseOrder, items)
        return ValidationResult.Success
    }
}
