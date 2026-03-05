package com.litvy.litvysales.catalog.product

import com.litvy.litvysales.domain.model.catalog.Product
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetProductsByBrandTest {

    private lateinit var repository: FakeProductRepository

    @Before
    fun setup() {
        repository = FakeProductRepository()
    }

    @Test
    fun should_return_products_by_brand() = runBlocking {

        repository.insert(Product(1, "Coca Cola", 1, 1000, 1500, false, false, true, 0, 0))
        repository.insert(Product(2,"Pepsi",1,1000,1500,false,false,true,0,0))
        repository.insert(Product(3,"Fanta",2,1000,1500,false,false,true,0,0))

        val result = repository.getByBrand(1).first()

        assertEquals(2, result.size)
    }
}