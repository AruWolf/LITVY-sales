package com.litvy.litvysales.inventory.stockMovement

import com.litvy.litvysales.domain.model.enums.StockMovementType
import com.litvy.litvysales.domain.model.inventory.StockMovement
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GenerateStockMovementTest {

    private lateinit var repository: FakeStockMovementRepository

    @Before
    fun setup() {
        repository = FakeStockMovementRepository()
    }

    @Test
    fun should_generate_stock_movement() = runBlocking {

        val movement = StockMovement(
            id = 1,
            productId = 1,
            batchId = 1,
            type = StockMovementType.PURCHASE,
            quantity = 10.0,
            createdAt = System.currentTimeMillis(),
            referenceId = 1,
            referenceType = "purchase",
            createdBy = 1
        )

        repository.generateMovement(movement)

        val result = repository.getStockMovement(1)

        assertNotNull(result)
        assertEquals(10.0, result?.quantity)
    }
}