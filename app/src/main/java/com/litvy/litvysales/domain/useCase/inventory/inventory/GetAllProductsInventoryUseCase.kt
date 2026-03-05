package com.litvy.litvysales.domain.useCase.inventory.inventory

import com.litvy.litvysales.domain.interfaces.inventory.InventoryRepository
import com.litvy.litvysales.domain.model.inventory.Inventory
import kotlinx.coroutines.flow.Flow

class GetAllProductsInventoryUseCase(
    private val repository: InventoryRepository
) {
    operator fun invoke(): Flow<List<Inventory>> {
        return repository.getAllProductsInventory()
    }

}