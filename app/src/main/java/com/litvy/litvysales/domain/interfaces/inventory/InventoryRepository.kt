package com.litvy.litvysales.domain.interfaces.inventory

import com.litvy.litvysales.domain.model.inventory.Inventory
import kotlinx.coroutines.flow.Flow

interface InventoryRepository {
    fun getInventoryByProduct(productId: Int): Flow<Inventory?>

    fun getAllProductsInventory(): Flow<List<Inventory>>
}