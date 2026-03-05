package com.litvy.litvysales.catalog.brand

import com.litvy.litvysales.domain.model.catalog.Brand
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CreateBrandTest {

    private lateinit var repository: FakeBrandRepository

    @Before
    fun setup() {
        repository = FakeBrandRepository()
    }

    @Test
    fun should_create_brand() = runBlocking {

        val brand = Brand(
            id = 1,
            name = "Coca Cola",
            subCategoryId = 1,
            createdAt = 0,
            updatedAt = 0
        )

        repository.insert(brand)

        val result = repository.getById(1)

        assertNotNull(result)
    }
}