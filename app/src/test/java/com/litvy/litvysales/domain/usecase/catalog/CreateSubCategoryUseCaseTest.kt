package com.litvy.litvysales.domain.usecase.catalog

import com.litvy.litvysales.domain.fake.FakeSubCategoryRepository
import com.litvy.litvysales.domain.useCase.catalog.subCategory.CreateSubCategoryUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.collections.first

class CreateSubCategoryUseCaseTest {

    private lateinit var repository: FakeSubCategoryRepository
    private lateinit var useCase: CreateSubCategoryUseCase

    @Before
    fun setup() {
        repository = FakeSubCategoryRepository()
        useCase = CreateSubCategoryUseCase(repository)
    }

    @Test
    fun `should create subcategory successfully`() = runBlocking {
        useCase("Sodas", 1)

        val result = repository.getByCategory(1)

        assertEquals(1, result.size)
        assertEquals("Sodas", result.first().name)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should fail when name is empty`() = runBlocking {
        useCase("   ", 1)
    }

    @Test(expected = IllegalStateException::class)
    fun `should fail when duplicate in same category`() = runBlocking {
        useCase("Sodas", 1)
        useCase("Sodas", 1)
    }
}