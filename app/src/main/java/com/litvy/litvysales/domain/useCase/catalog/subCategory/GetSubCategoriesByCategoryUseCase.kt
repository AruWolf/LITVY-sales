package com.litvy.litvysales.domain.useCase.catalog.subCategory

import com.litvy.litvysales.domain.interfaces.catalog.SubCategoryRepository
import com.litvy.litvysales.domain.model.catalog.SubCategory

class GetSubCategoriesByCategoryUseCase(
    private val repository: SubCategoryRepository
) {
    suspend operator fun invoke(categoryId: Int)
            : List<SubCategory> {

        return repository.getByCategory(categoryId)
    }
}