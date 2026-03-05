package com.litvy.litvysales.catalog.product

import com.litvy.litvysales.domain.model.catalog.Product
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeactivateProductTest {

    private lateinit var repository: FakeProductRepository

    @Before
    fun setup() {
        repository = FakeProductRepository()
    }

    @Test
    fun should_deactivate_product() = runBlocking {

        repository.insert(Product(1, "Coca Cola", 1, 1000, 1500, false, false, true, 0, 0))

        repository.deactivate(1,System.currentTimeMillis())

        val result = repository.getById(1)

        assertEquals(false, result?.active)
    }
}