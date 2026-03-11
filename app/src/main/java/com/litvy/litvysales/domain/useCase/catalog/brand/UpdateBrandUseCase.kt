package com.litvy.litvysales.domain.useCase.catalog.brand

import com.litvy.litvysales.domain.interfaces.catalog.BrandRepository
import com.litvy.litvysales.domain.validation.CommonValidators
import com.litvy.litvysales.domain.validation.ValidationBuilder
import com.litvy.litvysales.domain.validation.ValidationResult
import java.lang.IllegalStateException

class UpdateBrandUseCase(
    private val repository: BrandRepository
) {

    suspend operator fun invoke(
        id: Int,
        newName: String
    ): ValidationResult{

        val cleanName = newName.trim()
        val validator = ValidationBuilder()

        validator.add(CommonValidators.notBlank("name", cleanName))

        val existing = repository.getById(id) ?: throw IllegalStateException("Brand not found")

        validator.check(
            (existing.name != cleanName && !repository.existsByNameInSubCategory(cleanName, existing.subCategoryId)),
            "name",
            "La marca ya existe en la SubCategoria"
        )

        val result = validator.build()

        if(result is ValidationResult.Failure) return result

        repository.update(
         existing.copy(
            name = cleanName,
            updatedAt = System.currentTimeMillis()
        ))

        return ValidationResult.Success

    }
}