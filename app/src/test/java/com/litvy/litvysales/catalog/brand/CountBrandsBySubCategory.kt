package com.litvy.litvysales.catalog.brand

import com.litvy.litvysales.domain.model.catalog.Brand
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CountBrandsBySubCategoryTest {

    private lateinit var repository: FakeBrandRepository

    @Before
    fun setup() {
        repository = FakeBrandRepository()
    }

    @Test
    fun should_count_brands_in_subcategory() = runBlocking {

        repository.insert(Brand(1, "Coca Cola", 1, 0, 0))
        repository.insert(Brand(2,"Pepsi",1,0,0))
        repository.insert(Brand(3,"Lays",2,0,0))

        val result = repository.countBySubCategory(1)

        assertEquals(2, result)
    }
}