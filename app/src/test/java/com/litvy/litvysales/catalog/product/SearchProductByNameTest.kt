package com.litvy.litvysales.catalog.product

import com.litvy.litvysales.domain.model.catalog.Product
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SearchProductByNameTest {

    private lateinit var repository: FakeProductRepository

    @Before
    fun setup() {
        repository = FakeProductRepository()
    }

    @Test
    fun should_search_product_by_name() = runBlocking {

        repository.insert(Product(1, "Coca Cola", 1, 1000, 1500, false, false, true, 0, 0))
        repository.insert(Product(2,"Pepsi",1,1000,1500,false,false,true,0,0))

        val result = repository.searchByName("Coca").first()

        assertEquals(1, result.size)
    }
}