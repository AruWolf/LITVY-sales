package com.litvy.litvysales.catalog.product

import com.litvy.litvysales.domain.model.catalog.Product
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CountProductsByBrandTest {

    private lateinit var repository: FakeProductRepository

    @Before
    fun setup() {
        repository = FakeProductRepository()
    }

    @Test
    fun should_count_products_by_brand() = runBlocking {

        repository.insert(Product(1,"Coca Cola",1,1000,1500,false,false,true,0,0))
        repository.insert(Product(2, "Pepsi", 1, 1000, 1500, false, false, true, 0, 0))

        val result = repository.countByBrand(1)

        assertEquals(2, result)
    }
}