package com.litvy.litvysales.domain.useCase.catalog.product

import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository

class GetProductByIdUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: Int) = repository.getById(productId)
}
