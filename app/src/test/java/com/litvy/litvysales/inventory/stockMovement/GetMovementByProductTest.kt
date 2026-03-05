package com.litvy.litvysales.inventory.stockMovement

import com.litvy.litvysales.domain.model.enums.StockMovementType
import com.litvy.litvysales.domain.model.inventory.StockMovement
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetMovementByProductTest {

    private lateinit var repository: FakeStockMovementRepository

    @Before
    fun setup() {
        repository = FakeStockMovementRepository()
    }

    @Test
    fun should_get_movements_by_product() = runBlocking {

        repository.generateMovement(
            StockMovement(1, 1, 1, StockMovementType.PURCHASE, 10.0, 0, 1, "purchase", 1)
        )

        repository.generateMovement(
            StockMovement(2,1,1,StockMovementType.SALE,2.0,0,1,"sale",1)
        )

        val result = repository.getStockMovementByProduct(1).first()

        assertEquals(2, result.size)
    }
}