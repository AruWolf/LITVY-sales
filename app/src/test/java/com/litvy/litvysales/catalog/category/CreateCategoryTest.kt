package com.litvy.litvysales.catalog.category

import com.litvy.litvysales.domain.model.catalog.Category
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.*

class CreateCategoryTest {
    private lateinit var repository: FakeCategoryRepository

    @Before
    fun setup(){
        repository = FakeCategoryRepository()
    }

    @Test
    fun should_create_category() = runBlocking {
        val category = Category(
            id = 1,
            name = "Bebidas",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        repository.create(category)

        val result = repository.getById(1)

        assertNotNull(result)
        assertEquals("Bebidas", result?.name)
    }
}