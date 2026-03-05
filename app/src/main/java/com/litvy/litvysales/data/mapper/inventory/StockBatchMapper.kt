package com.litvy.litvysales.data.mapper.inventory

import com.litvy.litvysales.data.local.entity.inventory.StockBatchEntity
import com.litvy.litvysales.domain.model.inventory.StockBatch

fun StockBatch.toEntity(): StockBatchEntity =
    StockBatchEntity(
        id = id,
        productId = productId,
        quantity = quantity,
        expirationDate = expirationDate,
        purchaseItemId = purchaseItemId,
        createdAt = createdAt
    )

fun StockBatchEntity.toDomain(): StockBatch =
    StockBatch(
        id = id,
        productId = productId,
        quantity = quantity,
        expirationDate = expirationDate,
        purchaseItemId = purchaseItemId,
        createdAt = createdAt
    )