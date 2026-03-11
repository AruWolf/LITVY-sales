package com.litvy.litvysales.domain.useCase.catalog.brand

import com.litvy.litvysales.domain.interfaces.catalog.BrandRepository
import com.litvy.litvysales.domain.model.catalog.Brand
import com.litvy.litvysales.domain.validation.CommonValidators
import com.litvy.litvysales.domain.validation.ValidationBuilder
import com.litvy.litvysales.domain.validation.ValidationResult

class CreateBrandUseCase(
    private val repository: BrandRepository
) {

    suspend operator fun invoke(
        name: String,
        subCategoryId: Int
    ): ValidationResult{
        val cleanName = name.trim()
        val validator = ValidationBuilder()

        validator.add(CommonValidators.notBlank("name", cleanName))

        validator.check(
            !repository.existsByNameInSubCategory(cleanName, subCategoryId),
            "name",
            "La marca ya existe en la subCategoria"
        )

        val result = validator.build()

        if (result is ValidationResult.Failure) return result

        val now = System.currentTimeMillis()

        repository.insert(
         Brand(
            id = null,
            name = cleanName,
            subCategoryId = subCategoryId,
            createdAt = now,
            updatedAt = now
        ))

        return ValidationResult.Success

    }
}