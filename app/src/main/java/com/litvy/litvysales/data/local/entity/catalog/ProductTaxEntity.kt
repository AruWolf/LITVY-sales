package com.litvy.litvysales.data.local.entity.catalog

import androidx.room.*
import com.litvy.litvysales.data.local.entity.sales.TaxItemEntity

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