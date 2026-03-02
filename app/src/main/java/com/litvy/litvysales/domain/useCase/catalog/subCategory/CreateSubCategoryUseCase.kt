package com.litvy.litvysales.domain.useCase.catalog.subCategory

import com.litvy.litvysales.domain.interfaces.catalog.SubCategoryRepository
import com.litvy.litvysales.domain.model.catalog.SubCategory

class CreateSubCategoryUseCase(
    private val repository: SubCategoryRepository
) {

    suspend operator fun invoke(
        name: String,
        categoryId: Int
    ) {

        val cleanName = name.trim()

        require(cleanName.isNotEmpty()) {
            "SubCategory name cannot be empty"
        }

        if (repository.existsByNameInCategory(cleanName, categoryId)) {
            throw IllegalStateException(
                "SubCategory already exists in this category"
            )
        }

        val now = System.currentTimeMillis()

        val subCategory = SubCategory(
            id = null,
            name = cleanName,
            categoryId = categoryId,
            createdAt = now,
            updatedAt = now
        )

        repository.create(subCategory)
    }
}