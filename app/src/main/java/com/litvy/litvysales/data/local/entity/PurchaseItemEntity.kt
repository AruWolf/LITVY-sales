package com.litvy.litvysales.data.local.entity

import androidx.room.*

@Entity(
    tableName = "purchaseItem",
    foreignKeys = [
        ForeignKey(
            entity = PurchaseEntity::class,
            parentColumns = ["id"],
            childColumns = ["purchaseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("purchaseId"),
        Index("productId")
    ]
)
data class PurchaseItemEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val purchaseId: Int,

    val productId: Int,

    val quantity: Double,

    val unitPriceInCents: Long
)