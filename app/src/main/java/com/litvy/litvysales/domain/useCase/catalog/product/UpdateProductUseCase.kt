package com.litvy.litvysales.domain.useCase.catalog.product

import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository
import com.litvy.litvysales.domain.validation.CommonValidators
import com.litvy.litvysales.domain.validation.ValidationBuilder
import com.litvy.litvysales.domain.validation.ValidationResult

class UpdateProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        id: Int,
        name: String,
        brandId: Int,
        purchasePriceInCents: Long,
        salePriceInCents: Long,
        hasExpiration: Boolean,
        isWeighable: Boolean
    ): ValidationResult {

        val product = repository.getById(id)
            ?: throw IllegalArgumentException("Product not found")

        val cleanName = name.trim()
        val validator = ValidationBuilder()

        validator.add(CommonValidators.notBlank("name", cleanName))

        validator.add(CommonValidators.positive("salePriceInCents", salePriceInCents))
        validator.add(CommonValidators.positive("purchasePriceInCents", purchasePriceInCents))

        validator.checkWarning(
            product.purchasePriceInCents <= salePriceInCents,
            "purchasePriceInCents",
            "El precio de venta es menor o igual que el precio de compra"
            )

        validator.check(
            !repository.existsByNameInBrandExcludingId(cleanName, brandId, id),
            "name",
            "El producto ya existe en la marca"
        )

        val result = validator.build()

        if(result is ValidationResult.Failure && result.errors.isNotEmpty()) return result

        repository.update(
        product.copy(
            name = cleanName,
            brandId = brandId,
            purchasePriceInCents = purchasePriceInCents,
            salePriceInCents = salePriceInCents,
            updatedAt = System.currentTimeMillis(),
            hasExpiration = hasExpiration,
            isWeighable = isWeighable
        ))

        return result
    }
}