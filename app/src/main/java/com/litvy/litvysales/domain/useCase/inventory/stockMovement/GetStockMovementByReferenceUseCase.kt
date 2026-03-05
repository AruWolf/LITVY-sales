package com.litvy.litvysales.domain.useCase.inventory.stockMovement

import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.model.inventory.StockMovement
import kotlinx.coroutines.flow.Flow

class GetStockMovementByReferenceUseCase(
    private val repository: StockMovementRepository
) {
    operator fun invoke(referenceId: Int): Flow<List<StockMovement>>{
        return repository.getStockMovementByReference(referenceId)
    }
}