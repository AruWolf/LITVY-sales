package com.litvy.litvysales.data.mapper

import com.litvy.litvysales.data.local.entity.catalog.BrandEntity
import com.litvy.litvysales.domain.model.catalog.Brand
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