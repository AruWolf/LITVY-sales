package com.litvy.litvysales.catalog.product

import com.litvy.litvysales.domain.model.catalog.Product
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ExistsProductByNameInBrandTest {

    private lateinit var repository: FakeProductRepository

    @Before
    fun setup() {
        repository = FakeProductRepository()
    }

    @Test
    fun should_return_true_if_product_exists_in_brand() = runBlocking {

        repository.insert(Product(1, "Coca Cola", 1, 1000, 1500, false, false, true, 0, 0))

        val result = repository.existsByNameInBrand("Coca Cola",1)

        assertTrue(result)
    }
}