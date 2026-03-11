package com.litvy.litvysales.domain.useCase.catalog.product

import com.litvy.litvysales.domain.interfaces.catalog.BrandRepository
import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository
import com.litvy.litvysales.domain.model.catalog.Product
import com.litvy.litvysales.domain.validation.CommonValidators
import com.litvy.litvysales.domain.validation.ValidationBuilder
import com.litvy.litvysales.domain.validation.ValidationResult

class CreateProductUseCase(
    private val repository: ProductRepository,
    private val brandRepository: BrandRepository
) {
    suspend operator fun invoke(name: String,
                       brandId: Int,
                       purchasePriceInCents: Long,
                       salePriceInCents: Long,
                       hasExpiration: Boolean,
                       isWeighable: Boolean,
                       ): ValidationResult{

        val cleanName = name.trim()
        val validator = ValidationBuilder()

        validator.add(CommonValidators.notBlank("name", cleanName))

        validator.add(CommonValidators.positive("purchase", purchasePriceInCents))
        validator.add(CommonValidators.positive("sale", salePriceInCents))
        validator.add(CommonValidators.notBlank("purchase", purchasePriceInCents.toString()))
        validator.add(CommonValidators.notBlank("sale", salePriceInCents.toString()))

        validator.checkWarning(salePriceInCents >= purchasePriceInCents,
            "salePriceInCents",
            "El precio de venta es menor o igual que el precio de compra"
        )

        val brand = brandRepository.getById(brandId)
            ?: throw IllegalStateException("Brand does not exist")

        validator.check(
            !repository.existsByNameInBrand(cleanName,brandId),
            "name",
            "El producto ya existe en la Marca"
            )

        val result = validator.build()

        if(result is ValidationResult.Failure && result.errors.isNotEmpty()) return result

        val now = System.currentTimeMillis()

        repository.insert(
        Product(
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
        ))

        return result

    }
}