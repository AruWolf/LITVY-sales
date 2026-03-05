package com.litvy.litvysales.domain.useCase.catalog.brand

import com.litvy.litvysales.domain.interfaces.catalog.BrandRepository
import com.litvy.litvysales.domain.model.catalog.Brand

class GetBrandBySubCategoryUseCase(
    private val repository: BrandRepository
) {

    suspend operator fun invoke(subCategoryId: Int): List<Brand>{
        return repository.getBySubCategory(subCategoryId)
    }
}