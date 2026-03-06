package com.litvy.litvysales.domain.useCase.inventory

import com.litvy.litvysales.domain.interfaces.inventory.InventoryRepository
import com.litvy.litvysales.domain.model.inventory.Inventory
import kotlinx.coroutines.flow.Flow

class GetAllInventoryUseCase(
    private val inventoryRepository: InventoryRepository
) {

    operator fun invoke(): Flow<List<Inventory>> {
        return inventoryRepository.getAllProductsInventory()
    }

}