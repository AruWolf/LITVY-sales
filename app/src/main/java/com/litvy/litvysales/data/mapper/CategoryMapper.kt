package com.litvy.litvysales.data.mapper

import com.litvy.litvysales.data.local.entity.catalog.CategoryEntity
import com.litvy.litvysales.domain.model.catalog.Category

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