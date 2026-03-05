package com.litvy.litvysales.domain.useCase.catalog.brand

import com.litvy.litvysales.domain.interfaces.catalog.BrandRepository
import java.lang.IllegalStateException

class UpdateBrandUseCase(
    private val repository: BrandRepository
) {

    suspend operator fun invoke(
        id: Int,
        newName: String
    ){

        val cleanName = newName.trim()

        require(cleanName.isNotEmpty()){
            "Brand name cannot be empty"
        }

        val existing = repository.getById(id) ?: throw IllegalStateException("Brand not found")

        if(existing.name != cleanName && repository.existsByNameInSubCategory(cleanName, existing.subCategoryId)){
            throw kotlin.IllegalStateException("Brand already exists in this subCategory")
        }

        val updated = existing.copy(
            name = cleanName,
            updatedAt = System.currentTimeMillis()
        )

        repository.update(updated)

    }
}