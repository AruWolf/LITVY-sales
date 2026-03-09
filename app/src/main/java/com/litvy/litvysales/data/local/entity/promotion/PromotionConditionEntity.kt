package com.litvy.litvysales.data.local.entity.promotion

import androidx.room.*
import com.litvy.litvysales.data.local.entity.enums.PromotionConditionType

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

    val type: PromotionConditionType,

    val value: String
)