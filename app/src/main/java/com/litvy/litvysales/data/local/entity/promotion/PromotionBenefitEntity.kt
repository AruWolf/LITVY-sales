package com.litvy.litvysales.data.local.entity.promotion

import androidx.room.*

@Entity(
    tableName = "promotion_benefit",
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
data class PromotionBenefitEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val promotionId: Int,

    val discountPercentage: Double? = null,
    val discountAmountInCents: Long? = null,
    val freeQuantity: Int? = null
)