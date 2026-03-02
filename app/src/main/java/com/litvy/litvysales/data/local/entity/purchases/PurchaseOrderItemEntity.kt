package com.litvy.litvysales.data.local.entity.purchases

import androidx.room.*
import com.litvy.litvysales.data.local.entity.catalog.ProductEntity

@Entity(
    tableName = "purchaseOrderItem",
    foreignKeys = [
        ForeignKey(
            entity = PurchaseOrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["purchaseOrderId"],
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
        Index("purchaseOrderId"),
        Index("productId")
    ]
)
data class PurchaseOrderItemEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val purchaseOrderId: Int,
    val productId: Int,

    val quantity: Double
)