package com.litvy.litvysales.domain.usecase.catalog

import com.litvy.litvysales.domain.fake.FakeCategoryRepository
import com.litvy.litvysales.domain.useCase.catalog.category.CreateCategoryUseCase
import com.litvy.litvysales.domain.useCase.catalog.category.GetCategoriesUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetCategoriesUseCaseTest {

    private lateinit var repository: FakeCategoryRepository
    private lateinit var createUseCase: CreateCategoryUseCase
    private lateinit var getUseCase: GetCategoriesUseCase

    @Before
    fun setup() {
        repository = FakeCategoryRepository()
        createUseCase = CreateCategoryUseCase(repository)
        getUseCase = GetCategoriesUseCase(repository)
    }

    @Test
    fun `should return all created categories`() = runBlocking {
        createUseCase("Beverages")
        createUseCase("Snacks")

        val result = getUseCase()

        assertEquals(2, result.size)
        assertEquals("Beverages", result[0].name)
        assertEquals("Snacks", result[1].name)
    }
}