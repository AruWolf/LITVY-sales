package com.litvy.litvysales.domain.useCase.inventory.stockBatch

import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository
import com.litvy.litvysales.domain.interfaces.inventory.StockBatchRepository
import com.litvy.litvysales.domain.model.inventory.StockBatch
import com.litvy.litvysales.domain.validation.CommonValidators
import com.litvy.litvysales.domain.validation.ValidationBuilder
import com.litvy.litvysales.domain.validation.ValidationResult

class LoadStockBatchUseCase(
    private val stockBatchRepository: StockBatchRepository,
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(batch: StockBatch): ValidationResult {

        val validator = ValidationBuilder()

        validator.check(productRepository.existsById(batch.productId),
            "product",
            "Producto inexistente")
        validator.add(CommonValidators.positive("quantity", batch.quantity.toLong()))

        val today = System.currentTimeMillis()

        validator.add(CommonValidators.notBlank("expirationDate", batch.expirationDate.toString()))
        batch.expirationDate?.let {
            validator.check(it >= today,
                "expirationDate",
                "El producto ya está vencido")
        }

        val result = validator.build()

        if (result is ValidationResult.Failure && result.errors.isNotEmpty()) return result

        stockBatchRepository.loadBatch(
            StockBatch(
                id = null,
                productId = batch.productId,
                quantity = batch.quantity,
                expirationDate = batch.expirationDate,
                purchaseItemId = batch.purchaseItemId,
                createdAt = today
            )
        )

        return result
    }

}