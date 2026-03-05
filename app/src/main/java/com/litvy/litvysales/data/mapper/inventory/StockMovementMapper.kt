package com.litvy.litvysales.data.mapper.inventory

import com.litvy.litvysales.data.local.entity.inventory.StockMovementEntity
import com.litvy.litvysales.domain.model.inventory.StockMovement

fun StockMovement.toEntity(): StockMovementEntity =
    StockMovementEntity(
        id = id,
        productId = productId,
        batchId = batchId,
        type = type,
        quantity = quantity,
        createdAt = createdAt,
        referenceId = referenceId,
        referenceType = referenceType,
        createdBy = createdBy
    )

fun StockMovementEntity.toDomain(): StockMovement =
    StockMovement(
        id = id,
        productId = productId,
        batchId = batchId,
        type = type,
        quantity = quantity,
        createdAt = createdAt,
        referenceId = referenceId,
        referenceType = referenceType,
        createdBy = createdBy
    )