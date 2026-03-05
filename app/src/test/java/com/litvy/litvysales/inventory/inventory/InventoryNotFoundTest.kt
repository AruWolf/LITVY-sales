package com.litvy.litvysales.inventory.inventory

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

class InventoryNotFoundTest {
    private lateinit var repository: FakeInventoryRepository

    @Test
    fun should_return_null_when_inventory_not_found() = runBlocking {

        repository = FakeInventoryRepository()

        val result = repository.getInventoryByProduct(999).first()

        assertEquals(null, result)
    }
}