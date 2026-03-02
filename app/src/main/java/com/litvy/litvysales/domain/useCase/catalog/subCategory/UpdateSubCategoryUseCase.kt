package com.litvy.litvysales.domain.useCase.catalog.subCategory

import com.litvy.litvysales.domain.interfaces.catalog.SubCategoryRepository

class UpdateSubCategoryUseCase(
    private val repository: SubCategoryRepository
) {

    suspend operator fun invoke(
        id: Int,
        newName: String
    ) {

        val cleanName = newName.trim()

        require(cleanName.isNotEmpty()) {
            "SubCategory name cannot be empty"
        }

        val existing = repository.getById(id)
            ?: throw IllegalStateException("SubCategory not found")

        if (
            existing.name != cleanName &&
            repository.existsByNameInCategory(
                cleanName,
                existing.categoryId
            )
        ) {
            throw IllegalStateException(
                "SubCategory already exists in this category"
            )
        }

        val updated = existing.copy(
            name = cleanName,
            updatedAt = System.currentTimeMillis()
        )

        repository.update(updated)
    }
}