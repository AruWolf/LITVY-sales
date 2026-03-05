package com.litvy.litvysales.data.mapper.inventory

import com.litvy.litvysales.data.local.entity.inventory.InventoryEntity
import com.litvy.litvysales.domain.model.inventory.Inventory

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