package com.litvy.litvysales.data.local.entity

import androidx.room.*

@Entity(
    tableName = "productTax",
    primaryKeys = ["productId", "taxId"],
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TaxItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["taxId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("productId"),
        Index("taxId")
    ]
)
data class ProductTaxEntity(

    val productId: Int,
    val taxId: Int
)