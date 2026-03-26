package com.litvy.litvysales.domain.useCase.catalog.product

import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository

class GetActiveProductsWithBrandUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke() = repository.getActiveProductsWithBrand()
}