package com.litvy.litvysales.data.local.entity.inventory

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.litvy.litvysales.data.local.entity.catalog.ProductEntity
import com.litvy.litvysales.data.local.entity.enums.StockMovementType
import com.litvy.litvysales.data.local.entity.user.UserEntity

@Entity(
    tableName = "stockMovement",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = StockBatchEntity::class,
            parentColumns = ["id"],
            childColumns = ["batchId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["createdBy"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("productId"),
        Index("batchId"),
        Index("createdBy"),
        Index("createdAt")
    ]
)
data class StockMovementEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,

    val productId: Int,

    val batchId: Int?,

    val type: StockMovementType,

    val quantity: Double,

    val createdAt: Long,

    val referenceId: Int?, // saleId, purchaseId, etc
    val referenceType: String?, // "SALE", "PURCHASE"

    val createdBy: Int
)
