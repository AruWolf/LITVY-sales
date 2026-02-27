package com.litvy.litvysales.data.local.entity

import androidx.room.*

@Entity(
    tableName = "stockBatch",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PurchaseItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["purchaseItemId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("productId"),
        Index("expirationDate")
    ]
)
data class StockBatchEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val productId: Int,

    val quantity: Double,

    val expirationDate: Long?,

    val purchaseItemId: Int?,

    val createdAt: Long
)