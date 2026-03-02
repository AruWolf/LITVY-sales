package com.litvy.litvysales.domain.usecase.catalog

import com.litvy.litvysales.domain.fake.FakeCategoryRepository
import com.litvy.litvysales.domain.useCase.catalog.category.CreateCategoryUseCase
import com.litvy.litvysales.domain.useCase.catalog.category.UpdateCategoryUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UpdateCategoryUseCaseTest {

    private lateinit var repository: FakeCategoryRepository
    private lateinit var createUseCase: CreateCategoryUseCase
    private lateinit var updateUseCase: UpdateCategoryUseCase

    @Before
    fun setup() {
        repository = FakeCategoryRepository()
        createUseCase = CreateCategoryUseCase(repository)
        updateUseCase = UpdateCategoryUseCase(repository)
    }

    @Test
    fun `should update category name successfully`() = runBlocking {
        createUseCase("Beverages")

        val category = repository.getAll().first()

        updateUseCase(category.id!!, "Drinks")

        val updated = repository.getAll().first()

        assertEquals("Drinks", updated.name)
    }

    @Test(expected = IllegalStateException::class)
    fun `should throw when updating non existing category`() = runBlocking {
        updateUseCase(999, "Whatever")
    }

    @Test(expected = IllegalStateException::class)
    fun `should throw when updating to duplicated name`() = runBlocking {
        createUseCase("Beverages")
        createUseCase("Snacks")

        val category = repository.getAll().first()

        updateUseCase(category.id!!, "Snacks")
    }
}