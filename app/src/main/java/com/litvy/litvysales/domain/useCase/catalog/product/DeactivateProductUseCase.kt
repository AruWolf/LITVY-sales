package com.litvy.litvysales.domain.useCase.catalog.product

import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository

class DeactivateProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: Int) {

        val existing = repository.getById(productId)
            ?: throw IllegalStateException("Product not found")

        if (!existing.active) {
            return
        }

        repository.deactivate(
            productId,
            System.currentTimeMillis()
        )
    }
}