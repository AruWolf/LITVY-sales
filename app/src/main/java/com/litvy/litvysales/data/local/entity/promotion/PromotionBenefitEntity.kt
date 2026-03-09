package com.litvy.litvysales.data.local.entity.promotion

import androidx.room.*
import com.litvy.litvysales.data.local.entity.enums.PromotionBenefitType

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

    val type: PromotionBenefitType,

    val value: Long
)