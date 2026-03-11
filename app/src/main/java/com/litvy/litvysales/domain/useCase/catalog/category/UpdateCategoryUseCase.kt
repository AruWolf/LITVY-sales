package com.litvy.litvysales.domain.useCase.catalog.category

import com.litvy.litvysales.domain.interfaces.catalog.CategoryRepository
import com.litvy.litvysales.domain.validation.CommonValidators
import com.litvy.litvysales.domain.validation.ValidationBuilder
import com.litvy.litvysales.domain.validation.ValidationResult

class UpdateCategoryUseCase(
    private val repository: CategoryRepository
) {

    suspend operator fun invoke(id: Int, newName: String): ValidationResult {

        val cleanName = newName.trim()

        val validator = ValidationBuilder()

        validator.add(
            CommonValidators.notBlank(
                "name", cleanName
            )
        )


        val existing = repository.getById(id)
            ?: throw IllegalStateException("Category not found")

        if (existing.name != cleanName &&
            repository.existsByName(cleanName)
        ) {
            validator.check(
                false,
                "name",
                "La categoria ya existe"
            )
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