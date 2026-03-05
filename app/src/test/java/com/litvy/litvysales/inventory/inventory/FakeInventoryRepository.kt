package com.litvy.litvysales.inventory.inventory

import com.litvy.litvysales.domain.interfaces.inventory.InventoryRepository
import com.litvy.litvysales.domain.model.inventory.Inventory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeInventoryRepository: InventoryRepository {

    private val inventories = mutableListOf<Inventory>()

    fun insertInventory(inventory: Inventory){
        inventories.add(inventory)
    }

    override fun getInventoryByProduct(productId: Int): Flow<Inventory?> {
        return flowOf(inventories.find {it.productId == productId})
    }

    override fun getAllProductsInventory(): Flow<List<Inventory>> {
        return flowOf(inventories)
    }

}