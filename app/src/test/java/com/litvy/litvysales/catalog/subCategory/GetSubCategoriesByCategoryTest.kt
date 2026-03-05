package com.litvy.litvysales.catalog.subCategory

import com.litvy.litvysales.domain.model.catalog.SubCategory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetSubCategoriesByCategoryTest {

    private lateinit var repository: FakeSubCategoryRepository

    @Before
    fun setup() {
        repository = FakeSubCategoryRepository()
    }

    @Test
    fun should_return_subcategories_by_category() = runBlocking {

        repository.create(SubCategory(1, 1, "Gaseosas", 0, 0))
        repository.create(SubCategory(2, 1, "Jugos", 0, 0))
        repository.create(SubCategory(3, 2, "Snacks", 0, 0))

        val result = repository.getByCategory(1)

        assertEquals(2, result.size)
    }
}