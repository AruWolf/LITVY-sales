package com.litvy.litvysales.domain.useCase.inventory.stockMovement

import com.litvy.litvysales.data.local.entity.enums.StockMovementType
import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository
import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.model.inventory.StockMovement
import java.lang.IllegalStateException

class GenerateStockMovementUseCase(
    private val repository: StockMovementRepository,
    private val productRepository: ProductRepository
) {
    suspend fun invoke(
        productId: Int,
        batchId: Int,
        type: StockMovementType,
        quantity: Double,
        referenceId: Int,
        referenceType: String,
        userId: Int){

        require(quantity > 0){
            "Quantity cannot be less or equal to zero"
        }

        val product = productRepository.getById(productId)?: throw IllegalStateException("This product doesn't exist")

        val movement = StockMovement(
            id = null,
            productId = productId,
            batchId = batchId,
            type = type,
            quantity = quantity,
            createdAt = System.currentTimeMillis(),
            referenceId = referenceId,
            referenceType = referenceType,
            createdBy = userId
        )

        repository.generateMovement(movement)
    }

}