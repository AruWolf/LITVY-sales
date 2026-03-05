package com.litvy.litvysales.inventory.stockBatch

import com.litvy.litvysales.domain.model.inventory.StockBatch
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetBatchByProductTest {

    private lateinit var repository: FakeStockBatchRepository

    @Before
    fun setup() {
        repository = FakeStockBatchRepository()
    }

    @Test
    fun should_get_batches_by_product() = runBlocking {

        repository.loadBatch(
            StockBatch(1, 1, 10.0, 0, 1, System.currentTimeMillis())
        )

        repository.loadBatch(
            StockBatch(2,1,5.0,0,1,System.currentTimeMillis())
        )

        val result = repository.getBatchByProduct(1).first()

        assertEquals(2, result.size)
    }
}