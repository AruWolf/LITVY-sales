package com.litvy.litvysales.catalog.subCategory

import com.litvy.litvysales.domain.model.catalog.SubCategory
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

private val subCategoryRepository = FakeSubCategoryRepository()

class CreateSubCategoryTest {
    @Test
    fun should_create_subcategory() = runBlocking {

        val subCategory = SubCategory(
            id = 1,
            name = "Gaseosas",
            categoryId = 1,
            createdAt = 0,
            updatedAt = 0
        )

        subCategoryRepository.create(subCategory)

        val result = subCategoryRepository.getByCategory(1)

        assertEquals(1, result.size)
    }
}