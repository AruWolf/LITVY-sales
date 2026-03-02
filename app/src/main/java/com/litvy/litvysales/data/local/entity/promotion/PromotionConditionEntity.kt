package com.litvy.litvysales.data.local.entity.promotion

import androidx.room.*

@Entity(
    tableName = "promotion_condition",
    foreignKeys = [
        ForeignKey(
            entity = PromotionEntity::class,
            parentColumns = ["id"],
            childColumns = ["promotionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("promotionId")]
)
data class PromotionConditionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val promotionId: Int,

    val minQuantity: Int? = null,
    val maxQuantity: Int? = null,

    val requiredQuantity: Int? = null, // para 3x2

    val minSubtotalInCents: Long? = null
)