package com.litvy.litvysales.data.mapper

import com.litvy.litvysales.data.local.entity.catalog.SubCategoryEntity
import com.litvy.litvysales.domain.model.catalog.SubCategory

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