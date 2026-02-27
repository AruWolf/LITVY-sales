package com.litvy.litvysales.data.useCase.productUseCase

import com.litvy.litvysales.data.local.entity.ProductEntity
import com.litvy.litvysales.data.repository.ProductRepository

class CreateProductUseCase(
    private val repository: ProductRepository
) {

    suspend operator fun invoke(product: ProductEntity) {
        repository.create(product)
    }
}