package com.litvy.litvysales.domain.useCase.catalog.subCategory

import com.litvy.litvysales.domain.interfaces.catalog.SubCategoryRepository
import com.litvy.litvysales.domain.validation.CommonValidators
import com.litvy.litvysales.domain.validation.ValidationBuilder
import com.litvy.litvysales.domain.validation.ValidationResult

class UpdateSubCategoryUseCase(
    private val repository: SubCategoryRepository
) {

    suspend operator fun invoke(
        id: Int,
        newName: String
    ): ValidationResult {

        val cleanName = newName.trim()
        val validator = ValidationBuilder()

        validator.add(CommonValidators.notBlank("name", cleanName))

        val existing = repository.getById(id)
            ?: throw IllegalStateException("SubCategory not found")

        if (existing.name != cleanName && repository.existsByNameInCategory(cleanName, existing.categoryId)) {
            validator.check(false, "name", "El nombre ya existe en la categoria")
        }

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