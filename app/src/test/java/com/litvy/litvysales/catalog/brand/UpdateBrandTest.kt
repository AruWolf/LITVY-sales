package com.litvy.litvysales.catalog.brand

import com.litvy.litvysales.domain.model.catalog.Brand
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UpdateBrandTest {

    private lateinit var repository: FakeBrandRepository

    @Before
    fun setup() {
        repository = FakeBrandRepository()
    }

    @Test
    fun should_update_brand() = runBlocking {

        val brand = Brand(1, "Coca Cola", 1, 0, 0)

        repository.insert(brand)

        val updated = Brand(1,"Coca Cola Company",1,0,1)

        repository.update(updated)

        val result = repository.getById(1)

        assertEquals("Coca Cola Company", result?.name)
    }
}