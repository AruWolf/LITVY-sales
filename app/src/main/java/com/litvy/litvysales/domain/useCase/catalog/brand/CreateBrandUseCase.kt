package com.litvy.litvysales.domain.useCase.catalog.brand

import com.litvy.litvysales.domain.interfaces.catalog.BrandRepository
import com.litvy.litvysales.domain.model.catalog.Brand

class CreateBrandUseCase(
    private val repository: BrandRepository
) {

    suspend operator fun invoke(
        name: String,
        subCategoryId: Int
    ){
        val cleanName = name.trim()

        require(cleanName.isNotEmpty()){
            "Brand name cannot be empty"
        }

        if (repository.existsByNameInSubCategory(cleanName, subCategoryId)){
            throw IllegalStateException(
                "Brand already exists in this subCategory"
            )
        }

        val now = System.currentTimeMillis()

        val brand = Brand(
            id = null,
            name = cleanName,
            subCategoryId = subCategoryId,
            createdAt = now,
            updatedAt = now
        )

        repository.insert(brand)

    }
}