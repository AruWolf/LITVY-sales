package com.litvy.litvysales.data.local.entity.inventory

import androidx.room.*
import com.litvy.litvysales.data.local.entity.catalog.*

@Entity(
    tableName = "inventory",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("productId")]
)
data class InventoryEntity(
    @PrimaryKey
    val productId: Int,

    val stock: Double,

    val updatedAt: Long
)