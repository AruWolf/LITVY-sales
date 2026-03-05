package com.litvy.litvysales.domain.useCase.catalog.product

import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository

class UpdateProductUseCase(
    private val repository: ProductRepository
) {
    suspend fun invoke(
        id: Int,
        name: String,
        brandId: Int,
        purchasePriceInCents: Long,
        salePriceInCents: Long
    ) {
        val product = repository.getById(id)
            ?: throw IllegalArgumentException("Product not found")

        val cleanName = name.trim()
        require(cleanName.isNotEmpty())

        if(repository.existsByNameInBrandExcludingId(cleanName, brandId, id)){
            throw IllegalStateException("Product already exists in this brand")
        }

        val updated = product.copy(
            name = cleanName,
            brandId = brandId,
            purchasePriceInCents = purchasePriceInCents,
            salePriceInCents = salePriceInCents,
            updatedAt = System.currentTimeMillis()
        )

        repository.update(updated)
    }
}