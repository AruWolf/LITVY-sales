package com.litvy.litvysales.data.local.entity.sales

import androidx.room.*
import com.litvy.litvysales.data.local.entity.promotion.*

@Entity(
    tableName = "sale_promotion",
    foreignKeys = [
        ForeignKey(
            entity = SaleEntity::class,
            parentColumns = ["id"],
            childColumns = ["saleId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PromotionEntity::class,
            parentColumns = ["id"],
            childColumns = ["promotionId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("saleId"),
        Index("promotionId")
    ]
)
data class SalePromotionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val saleId: Int,
    val promotionId: Int,

    val discountAppliedInCents: Long
)