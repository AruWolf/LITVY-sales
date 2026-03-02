package com.litvy.litvysales.data.local.entity.promotion

import androidx.room.*
import com.litvy.litvysales.data.local.entity.catalog.*
import com.litvy.litvysales.data.local.entity.inventory.*

@Entity(
    tableName = "promotion_target",
    foreignKeys = [
        ForeignKey(
            entity = PromotionEntity::class,
            parentColumns = ["id"],
            childColumns = ["promotionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BrandEntity::class,
            parentColumns = ["id"],
            childColumns = ["brandId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = StockBatchEntity::class,
            parentColumns = ["id"],
            childColumns = ["batchId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("promotionId"),
        Index("productId"),
        Index("brandId"),
        Index("categoryId"),
        Index("batchId")
    ]
)
data class PromotionTargetEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val promotionId: Int,

    val productId: Int? = null,
    val brandId: Int? = null,
    val categoryId: Int? = null,
    val batchId: Int? = null
)