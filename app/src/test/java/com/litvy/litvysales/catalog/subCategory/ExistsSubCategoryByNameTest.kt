package com.litvy.litvysales.catalog.subCategory

import com.litvy.litvysales.domain.model.catalog.SubCategory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ExistsSubCategoryByNameTest {

    private lateinit var repository: FakeSubCategoryRepository

    @Before
    fun setup() {
        repository = FakeSubCategoryRepository()
    }

    @Test
    fun should_return_true_if_subcategory_exists_in_category() = runBlocking {

        repository.create(
            SubCategory(1, 1, "Gaseosas", 0, 0)
        )

        val result = repository.existsByNameInCategory("Gaseosas", 1)

        assertTrue(result)
    }
}