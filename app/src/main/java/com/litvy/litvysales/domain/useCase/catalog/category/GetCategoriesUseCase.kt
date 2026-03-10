package com.litvy.litvysales.domain.useCase.catalog.category

import com.litvy.litvysales.domain.interfaces.catalog.CategoryRepository
import com.litvy.litvysales.domain.model.catalog.Category

class GetCategoriesUseCase(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): List<Category> {
        return repository.getAll()
    }
}