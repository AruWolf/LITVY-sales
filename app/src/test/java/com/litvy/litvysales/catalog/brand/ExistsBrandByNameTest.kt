package com.litvy.litvysales.catalog.brand

import com.litvy.litvysales.domain.model.catalog.Brand
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ExistsBrandByNameTest {

    private lateinit var repository: FakeBrandRepository

    @Before
    fun setup() {
        repository = FakeBrandRepository()
    }

    @Test
    fun should_return_true_if_brand_exists_in_subcategory() = runBlocking {

        repository.insert(
            Brand(1, "Coca Cola", 1, 0, 0)
        )

        val result = repository.existsByNameInSubCategory("Coca Cola",1)

        assertTrue(result)
    }
}