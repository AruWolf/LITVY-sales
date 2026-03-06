package com.litvy.litvysales.domain.useCase.inventory.inventory

import com.litvy.litvysales.domain.interfaces.inventory.InventoryRepository
import com.litvy.litvysales.domain.model.inventory.Inventory
import kotlinx.coroutines.flow.Flow

class GetInventoryByProductUseCase(
    private val inventoryRepository: InventoryRepository
) {

    operator fun invoke(productId: Int): Flow<Inventory?> {
        return inventoryRepository.getInventoryByProduct(productId)
    }

}