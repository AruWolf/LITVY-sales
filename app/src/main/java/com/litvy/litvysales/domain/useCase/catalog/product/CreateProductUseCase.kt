package com.litvy.litvysales.domain.useCase.catalog.product

import com.litvy.litvysales.domain.interfaces.catalog.BrandRepository
import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository
import com.litvy.litvysales.domain.model.catalog.Product

class CreateProductUseCase(
    private val repository: ProductRepository,
    private val brandRepository: BrandRepository
) {
    suspend fun invoke(name: String,
                       brandId: Int,
                       purchasePriceInCents: Long,
                       salePriceInCents: Long,
                       hasExpiration: Boolean,
                       isWeighable: Boolean,
                       ){

        val cleanName = name.trim()

        require(cleanName.isNotEmpty()){
            "Product name cannot be empty"
        }

        require(purchasePriceInCents >= 0){
            "Purchase price cannot be negative"
        }

        require(salePriceInCents >= 0){
            "Sale price cannot be negative"
        }

        val brand = brandRepository.getById(brandId)
            ?: throw IllegalStateException("Brand does not exist")

        if(repository.existsByNameInBrand(cleanName, brandId)){
            throw IllegalStateException("This product already exists in this brand")
        }
        val now = System.currentTimeMillis()

        val product = Product(
            id = null,
            name = cleanName,
            brandId = brandId,
            purchasePriceInCents = purchasePriceInCents,
            salePriceInCents = salePriceInCents,
            hasExpiration = hasExpiration,
            isWeighable = isWeighable,
            active = true, // Al crearse un producto, este siempre empieza activo.
            createdAt = now,
            updatedAt = now
        )

        repository.insert(product)

    }
}