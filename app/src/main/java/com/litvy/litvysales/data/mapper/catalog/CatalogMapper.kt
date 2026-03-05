package com.litvy.litvysales.data.mapper.catalog

import com.litvy.litvysales.data.local.entity.catalog.*
import com.litvy.litvysales.domain.model.catalog.*

fun CategoryEntity.toDomain(): Category =
    Category(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun Category.toEntity(): CategoryEntity =
    CategoryEntity(
        id = id ?: 0,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun Brand.toEntity(): BrandEntity =
    BrandEntity(
        id = id,
        name = name,
        subCategoryId = subCategoryId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun BrandEntity.toDomain(): Brand =
    Brand(
        id = id,
        name = name,
        subCategoryId = subCategoryId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )


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


fun SubCategoryEntity.toDomain(): SubCategory =
    SubCategory(
        id = id,
        name = name,
        categoryId = categoryId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun SubCategory.toEntity(): SubCategoryEntity =
    SubCategoryEntity(
        id = id ?: 0,
        name = name,
        categoryId = categoryId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )