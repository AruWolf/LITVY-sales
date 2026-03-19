package com.litvy.litvysales.domain.useCase.inventory.stockMovement

import com.litvy.litvysales.domain.filter.inventory.StockMovementFilter
import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.model.inventory.StockMovement
import kotlinx.coroutines.flow.Flow

class GetStockMovement(
    private val repository: StockMovementRepository){

    operator fun invoke(filter: StockMovementFilter): Flow<List<StockMovement?>> {
        return repository.getStockMovements(filter)
    }

}