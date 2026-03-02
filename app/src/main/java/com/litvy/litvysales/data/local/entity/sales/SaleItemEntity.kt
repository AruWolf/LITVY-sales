package com.litvy.litvysales.data.local.entity.sales

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.litvy.litvysales.data.local.entity.util.PaymentMethodEntity
import com.litvy.litvysales.data.local.entity.catalog.ProductEntity

@Entity(
    tableName = "saleItem",
    foreignKeys = [
        ForeignKey(
            entity = SaleEntity::class,
            parentColumns = ["id"],
            childColumns = ["saleId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = PaymentMethodEntity::class,
            parentColumns = ["id"],
            childColumns = ["paymentMethodId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("saleId"),
        Index("productId")
    ]

)
data class SaleItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val saleId: Int,
    val productId: Int,
    val paymentMethodId: Int,
    val quantity: Double,
    val unitPriceInCents: Long,
    val discountAppliedInCents: Long = 0,
    val originalUnitPriceInCents: Long,
    val totalInCents: Long
)