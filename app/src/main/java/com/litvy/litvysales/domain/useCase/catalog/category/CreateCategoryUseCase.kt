package com.litvy.litvysales.domain.useCase.catalog.category

import com.litvy.litvysales.domain.interfaces.catalog.CategoryRepository
import com.litvy.litvysales.domain.model.catalog.Category

class CreateCategoryUseCase(
    private val repository: CategoryRepository
) {

    suspend operator fun invoke(name: String) {

        val cleanName = name.trim()

        require(cleanName.isNotEmpty()) {
            "Category name cannot be empty"
        }

        if (repository.existsByName(cleanName)) {
            throw IllegalStateException("Category already exists")
        }

        val now = System.currentTimeMillis()

        val category = Category(
            id = null,
            name = cleanName,
            createdAt = now,
            updatedAt = now
        )

        repository.create(category)
    }
}