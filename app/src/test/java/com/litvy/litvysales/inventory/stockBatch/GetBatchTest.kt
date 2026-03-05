package com.litvy.litvysales.inventory.stockBatch

import com.litvy.litvysales.domain.model.inventory.StockBatch
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetBatchTest {

    private lateinit var repository: FakeStockBatchRepository

    @Before
    fun setup() {
        repository = FakeStockBatchRepository()
    }

    @Test
    fun should_get_batch_by_id() = runBlocking {

        val batch = StockBatch(
            id = 1,
            productId = 1,
            quantity = 5.0,
            expirationDate = 0,
            purchaseItemId = 1,
            createdAt = System.currentTimeMillis()
        )

        repository.loadBatch(batch)

        val result = repository.getBatch(1)

        assertEquals(5.0, result?.quantity)
    }
}