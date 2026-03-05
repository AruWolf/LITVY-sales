package com.litvy.litvysales.domain.useCase.catalog.product

import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository
import com.litvy.litvysales.domain.model.catalog.Product
import kotlinx.coroutines.flow.Flow

class GetActiveProductsUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.getActiveProducts()
    }
}