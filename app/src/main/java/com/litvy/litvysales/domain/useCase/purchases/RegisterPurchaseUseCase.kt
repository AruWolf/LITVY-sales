package com.litvy.litvysales.domain.useCase.purchases

import com.litvy.litvysales.domain.interfaces.inventory.StockBatchRepository
import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.interfaces.purchases.PurchaseRepository
import com.litvy.litvysales.domain.model.enums.StockMovementType
import com.litvy.litvysales.domain.model.inventory.StockBatch
import com.litvy.litvysales.domain.model.inventory.StockMovement
import com.litvy.litvysales.domain.model.purchases.PurchaseItem
import com.litvy.litvysales.domain.model.purchases.Purchase
import com.litvy.litvysales.domain.validation.CommonValidators
import com.litvy.litvysales.domain.validation.ValidationBuilder
import com.litvy.litvysales.domain.validation.ValidationResult

class RegisterPurchaseUseCase(

    private val purchaseRepository: PurchaseRepository,
    private val stockBatchRepository: StockBatchRepository,
    private val stockMovementRepository: StockMovementRepository

) {

    suspend operator fun invoke(
        purchase: Purchase,
        purchaseItems: List<PurchaseItem>,
        batches: List<StockBatch>
    ): ValidationResult {

        val validator = ValidationBuilder()

        validator.check(
            purchase.providerId > 0,
            "providerId",
            "Debe seleccionarse un proveedor valido"
        )
        validator.check(
            purchase.invoiceTypeId > 0,
            "invoiceTypeId",
            "Debe seleccionarse un tipo de factura"
        )
        validator.check(
            purchase.paymentMethodId > 0,
            "paymentMethodId",
            "Debe seleccionarse un metodo de pago"
        )
        validator.add(
            CommonValidators.positive(
                field = "createdBy",
                value = purchase.createdBy.toLong()
            )
        )
        validator.check(
            purchaseItems.isNotEmpty(),
            "items",
            "La compra debe contener al menos un producto"
        )
        validator.check(
            batches.size == purchaseItems.size,
            "items",
            "La compra debe tener un lote por cada item cargado"
        )
        purchase.salesRepName?.let {
            validator.check(
                it.isNotBlank(),
                "salesRepName",
                "El vendedor/preventista no puede estar vacio"
            )
        }

        val result = validator.build()
        if (result is ValidationResult.Failure && result.errors.isNotEmpty()) {
            return result
        }

        val purchaseId = purchaseRepository.create(purchase, purchaseItems).toInt()

        batches.forEach { batch ->
            val batchId = stockBatchRepository.loadBatch(
                batch.copy(purchaseItemId = null)
            ).toInt()

            val movement = StockMovement(
                id = 0,
                productId = batch.productId,
                batchId = batchId,
                type = StockMovementType.PURCHASE,
                quantity = batch.quantity,
                createdAt = System.currentTimeMillis(),
                referenceId = purchaseId,
                referenceType = "PURCHASE",
                createdBy = purchase.createdBy
            )

            stockMovementRepository.generateMovement(movement)
        }

        return ValidationResult.Success
    }
}
