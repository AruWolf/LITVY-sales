package com.litvy.litvysales.inventory.inventory

import com.litvy.litvysales.domain.model.inventory.Inventory
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetAllInventoryTest {
    private lateinit var repository: FakeInventoryRepository

    @Test
    fun should_get_all_inventories() = runBlocking {
        repository = FakeInventoryRepository()

        val inventory = Inventory(
            productId = 1,
            stock = 12.5,
            updatedAt = System.currentTimeMillis()
        )

        repository.insertInventory(inventory)

        val inventory2 = Inventory(
            productId = 3,
            stock = 15.8,
            updatedAt = System.currentTimeMillis()
        )

        repository.insertInventory(inventory2)

        val result = repository.getAllProductsInventory().first()

        assertNotNull(result)
    }
}