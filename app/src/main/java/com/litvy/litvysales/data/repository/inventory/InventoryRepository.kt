package com.litvy.litvysales.data.repository.inventory

import com.litvy.litvysales.data.local.dao.inventory.InventoryDao
import com.litvy.litvysales.data.mapper.inventory.toDomain
import com.litvy.litvysales.domain.interfaces.inventory.InventoryRepository
import com.litvy.litvysales.domain.model.inventory.Inventory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InventoryRepository(
    private val inventoryDao: InventoryDao,
): InventoryRepository {
    override fun getInventoryByProduct(productId: Int): Flow<Inventory?> {
        return inventoryDao.getByProductId(productId).map {it?.toDomain()}
    }
    override fun getAllProductsInventory(): Flow<List<Inventory>> {
        return inventoryDao.getAllProductsInventory().map {list -> list.map {it.toDomain()}}
    }
}