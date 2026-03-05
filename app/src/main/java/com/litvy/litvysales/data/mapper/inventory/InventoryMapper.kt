package com.litvy.litvysales.data.mapper.inventory

import com.litvy.litvysales.data.local.entity.enums.StockMovementType as DataStockMovementType
import com.litvy.litvysales.domain.model.enums.StockMovementType as DomainStockMovementType
import com.litvy.litvysales.data.local.entity.inventory.*
import com.litvy.litvysales.domain.model.inventory.*


fun Inventory.toEntity(): InventoryEntity =
    InventoryEntity(
        productId = productId,
        stock = stock,
        updatedAt = updatedAt
    )

fun InventoryEntity.toDomain(): Inventory =
    Inventory(
        productId = productId,
        stock = stock,
        updatedAt = updatedAt
    )


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


fun StockMovement.toEntity(): StockMovementEntity =
    StockMovementEntity(
        id = id,
        productId = productId,
        batchId = batchId,
        type = type.toEntity(),
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
        type = type.toDomain(),
        quantity = quantity,
        createdAt = createdAt,
        referenceId = referenceId,
        referenceType = referenceType,
        createdBy = createdBy
    )


fun DomainStockMovementType.toEntity(): DataStockMovementType =
    when (this) {
        DomainStockMovementType.SALE -> DataStockMovementType.SALE
        DomainStockMovementType.PURCHASE -> DataStockMovementType.PURCHASE
        DomainStockMovementType.ADJUSTMENT -> DataStockMovementType.ADJUSTMENT
        DomainStockMovementType.LOSS -> DataStockMovementType.LOSS
    }

fun DataStockMovementType.toDomain(): DomainStockMovementType =
    when (this) {
        DataStockMovementType.SALE -> DomainStockMovementType.SALE
        DataStockMovementType.PURCHASE -> DomainStockMovementType.PURCHASE
        DataStockMovementType.ADJUSTMENT -> DomainStockMovementType.ADJUSTMENT
        DataStockMovementType.LOSS -> DomainStockMovementType.LOSS
    }