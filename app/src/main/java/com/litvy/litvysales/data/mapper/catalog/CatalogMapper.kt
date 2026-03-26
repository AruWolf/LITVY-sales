package com.litvy.litvysales.data.mapper.catalog

import com.litvy.litvysales.data.local.entity.catalog.*
import com.litvy.litvysales.domain.model.catalog.*
import com.litvy.litvysales.data.local.relation.ProductWithBrand as DataProductWithBrand
import com.litvy.litvysales.domain.model.catalog.ProductWithBrand as DomProductWithBrand

fun CategoryEntity.toDomain(): Category =
    Category(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun Category.toEntity(): CategoryEntity =
    CategoryEntity(
        id = id,
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

fun DataProductWithBrand.toDomain(): DomProductWithBrand {
    return DomProductWithBrand(
        id = product.id ?: 0,
        name = product.name,
        brandId = product.brandId,
        brandName = brand.name,
        purchasePriceInCents = product.purchasePriceInCents,
        salePriceInCents = product.salePriceInCents,
        hasExpiration = product.hasExpiration,
        isWeighable = product.isWeighable,
        active = product.active,
        createdAt = product.createdAt,
        updatedAt = product.updatedAt
    )
}


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
        id = id,
        name = name,
        categoryId = categoryId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )