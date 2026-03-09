package com.litvy.litvysales.data.local.entity.promotion

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.litvy.litvysales.data.local.entity.catalog.ProductEntity

@Entity(
    tableName = "promotion_product_requirement",
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
        )
    ],
    indices = [
        Index(value = ["promotionId","productId"], unique = true)
    ]
)
data class PromotionProductRequirementEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val promotionId: Int,

    val productId: Int,

    val requiredQuantity: Int
)