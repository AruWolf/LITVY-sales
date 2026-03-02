package com.litvy.litvysales.domain.useCase.catalog.category

import com.litvy.litvysales.domain.interfaces.catalog.CategoryRepository

class UpdateCategoryUseCase(
    private val repository: CategoryRepository
) {

    suspend operator fun invoke(id: Int, newName: String) {

        val cleanName = newName.trim()

        require(cleanName.isNotEmpty()) {
            "Category name cannot be empty"
        }

        val existing = repository.getById(id)
            ?: throw IllegalStateException("Category not found")

        if (existing.name != cleanName &&
            repository.existsByName(cleanName)
        ) {
            throw IllegalStateException("Category already exists")
        }

        val updated = existing.copy(
            name = cleanName,
            updatedAt = System.currentTimeMillis()
        )

        repository.update(updated)
    }
}