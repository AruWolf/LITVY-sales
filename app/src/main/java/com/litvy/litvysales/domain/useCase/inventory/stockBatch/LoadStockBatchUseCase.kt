package com.litvy.litvysales.domain.useCase.inventory.stockBatch

import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository
import com.litvy.litvysales.domain.interfaces.inventory.StockBatchRepository
import com.litvy.litvysales.domain.model.inventory.StockBatch
import java.lang.IllegalStateException

class LoadStockBatchUseCase(
    private val repository: StockBatchRepository,
    private val productRepository: ProductRepository
) {
    suspend fun invoke(productId: Int, quantity: Double, expirationDate: Long?, purchaseItemId: Int){

        require(quantity > 0){
            "Invalid quantity"
        }

        val product = productRepository.getById(productId)
            ?: throw IllegalStateException("This product doesn´t exist")

        //TODO: Validación de purchaseItem existente. Hacer cuando este el modulo de compras.

        val stockBatch = StockBatch(
            id = null,
            productId = productId,
            quantity = quantity,
            expirationDate = expirationDate,
            purchaseItemId = purchaseItemId,
            createdAt = System.currentTimeMillis()
        )

        repository.loadBatch(stockBatch)
    }
}