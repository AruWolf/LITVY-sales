package com.litvy.litvysales.data.mapper

import com.litvy.litvysales.data.local.entity.catalog.ProductEntity
import com.litvy.litvysales.domain.model.catalog.Product

fun Product.toEntity(): ProductEntity =
    ProductEntity(
        id = id,
        name = name,
        brandId = brandId,
        purchasePriceInCents = purchasePriceInCents,
        salePriceInCents = salePriceInCents,
        hasExpiration = hasExpiration,
        isWeighable = isWeighable,
        active = active,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun ProductEntity.toDomain(): Product =
    Product(
        id = id,
        name = name,
        brandId = brandId,
        purchasePriceInCents = purchasePriceInCents,
        salePriceInCents = salePriceInCents,
        hasExpiration = hasExpiration,
        isWeighable = isWeighable,
        active = active,
        createdAt = createdAt,
        updatedAt = updatedAt
    )