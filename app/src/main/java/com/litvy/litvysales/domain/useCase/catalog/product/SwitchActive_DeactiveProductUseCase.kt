package com.litvy.litvysales.domain.useCase.catalog.product

import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository

class SwitchActive_DeactiveProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: Int) {

        val existing = repository.getById(productId)
            ?: throw IllegalStateException("Product not found")

        if(existing.active){
        repository.deactivate(
            productId,
            System.currentTimeMillis()
        )
        }else{
            repository.activate(productId, System.currentTimeMillis())
        }
    }
}