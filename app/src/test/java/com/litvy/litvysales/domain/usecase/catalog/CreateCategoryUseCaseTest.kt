package com.litvy.litvysales.domain.usecase.catalog

import com.litvy.litvysales.domain.fake.FakeCategoryRepository
import com.litvy.litvysales.domain.useCase.catalog.category.CreateCategoryUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CreateCategoryUseCaseTest {

    private lateinit var repository: FakeCategoryRepository
    private lateinit var useCase: CreateCategoryUseCase

    @Before
    fun setup() {
        repository = FakeCategoryRepository()
        useCase = CreateCategoryUseCase(repository)
    }

    @Test
    fun `should create category successfully`() = runBlocking {
        useCase("Beverages")

        val categories = repository.getAll()

        assertEquals(1, categories.size)
        assertEquals("Beverages", categories.first().name)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw exception when name is empty`() = runBlocking {
        useCase("   ")
    }

    @Test(expected = IllegalStateException::class)
    fun `should throw exception when category already exists`() = runBlocking {
        useCase("Beverages")
        useCase("Beverages")
    }
}