package com.litvy.litvysales.domain.useCase.catalog.subCategory

import com.litvy.litvysales.domain.interfaces.catalog.CategoryRepository
import com.litvy.litvysales.domain.interfaces.catalog.SubCategoryRepository
import com.litvy.litvysales.domain.model.catalog.SubCategory
import com.litvy.litvysales.domain.validation.CommonValidators
import com.litvy.litvysales.domain.validation.ValidationBuilder
import com.litvy.litvysales.domain.validation.ValidationResult

class CreateSubCategoryUseCase(
    private val repository: SubCategoryRepository,
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(
        name: String,
        categoryId: Int
    ): ValidationResult {

        val cleanName = name.trim()
        val validator = ValidationBuilder()

        validator.add(
            CommonValidators.notBlank("name", cleanName)
        )

        if (!categoryRepository.existsById(categoryId)) {
            throw IllegalStateException("Category does not exist")
        }

        if (repository.existsByNameInCategory(cleanName, categoryId)) {
            validator.check(false, "name", "El nombre ya existe en la categoria")
        }

        val result = validator.build()

        if(result is ValidationResult.Failure) return result

        val now = System.currentTimeMillis()

        repository.create(
         SubCategory(
            id = null,
            name = cleanName,
            categoryId = categoryId,
            createdAt = now,
            updatedAt = now
        ))

        return ValidationResult.Success
    }
}