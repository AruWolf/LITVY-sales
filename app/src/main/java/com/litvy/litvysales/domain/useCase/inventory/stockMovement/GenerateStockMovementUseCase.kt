package com.litvy.litvysales.domain.useCase.inventory.stockMovement

import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.model.inventory.StockMovement

class GenerateStockMovementUseCase(
    private val stockMovementRepository: StockMovementRepository
) {

    suspend operator fun invoke(movement: StockMovement) {

        if (movement.quantity <= 0) {
            throw IllegalArgumentException("Movement quantity must be greater than 0")
        }

        stockMovementRepository.generateMovement(movement)
    }

}