package com.litvy.litvysales.catalog.category

import com.litvy.litvysales.domain.model.catalog.Category
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ExistsCategoryByNameTest {

    private lateinit var repository: FakeCategoryRepository

    @Before
    fun setup() {
        repository = FakeCategoryRepository()
    }

    @Test
    fun should_return_true_if_category_name_exists() = runBlocking {

        repository.create(Category(1, "Bebidas", 0, 0))

        val result = repository.existsByName("Bebidas")

        assertTrue(result)
    }
}