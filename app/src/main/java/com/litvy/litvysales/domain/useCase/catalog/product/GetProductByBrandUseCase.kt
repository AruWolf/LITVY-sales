package com.litvy.litvysales.domain.useCase.catalog.product

import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository
import com.litvy.litvysales.domain.model.catalog.Product
import kotlinx.coroutines.flow.Flow

class GetProductByBrandUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(brandId: Int): Flow<List<Product>> {
        return repository.getByBrand(brandId)
    }
}