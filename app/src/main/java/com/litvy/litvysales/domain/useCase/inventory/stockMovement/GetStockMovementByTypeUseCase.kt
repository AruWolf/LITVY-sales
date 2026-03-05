package com.litvy.litvysales.domain.useCase.inventory.stockMovement

import com.litvy.litvysales.domain.model.enums.StockMovementType
import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.model.inventory.StockMovement
import kotlinx.coroutines.flow.Flow

class GetStockMovementByTypeUseCase(
    private val repository: StockMovementRepository
) {
    operator fun invoke(type: StockMovementType): Flow<List<StockMovement>>{
        return repository.getStockMovementByType(type)
    }
}