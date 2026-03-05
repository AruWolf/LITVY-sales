package com.litvy.litvysales.catalog.category

import com.litvy.litvysales.domain.model.catalog.Category
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetAllCategoriesTest {

    private var repository = FakeCategoryRepository()
    @Test
    fun should_return_all_categories() = runBlocking {
        repository.create(Category(1, "Bebidas", 0, 0))
        repository.create(Category(2, "Bebidas Alchoholicas", 0, 0))

        val result = repository.getAll()

        assertEquals(2, result.size)
    }
}