package com.litvy.litvysales.inventory.inventory

import com.litvy.litvysales.domain.model.inventory.Inventory
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetInventoryByProductTest {
    private lateinit var repository: FakeInventoryRepository

    @Test
    fun should_get_inventory_by_product() = runBlocking{

        repository = FakeInventoryRepository()

        val inventory = Inventory(
            productId = 1,
            stock = 10.2,
            updatedAt = System.currentTimeMillis()
        )

        repository.insertInventory(inventory)

        val result = repository.getInventoryByProduct(1).first()

        assertEquals(10.2, result?.stock)
    }
}