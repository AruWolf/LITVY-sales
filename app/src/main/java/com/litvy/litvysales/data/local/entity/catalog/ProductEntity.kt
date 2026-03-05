package com.litvy.litvysales.data.local.entity.catalog

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "product",
    foreignKeys = [
        ForeignKey(
            entity = BrandEntity::class,
            parentColumns = ["id"],
            childColumns = ["brandId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["brandId", "name"], unique = true)
    ])
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,
    val name: String,
    val brandId: Int,
    val purchasePriceInCents: Long,
    val salePriceInCents: Long,
    val hasExpiration: Boolean,
    val isWeighable: Boolean,
    val active: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)