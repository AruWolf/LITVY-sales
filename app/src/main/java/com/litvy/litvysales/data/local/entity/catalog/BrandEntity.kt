package com.litvy.litvysales.data.local.entity.catalog

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "brand",
    foreignKeys = [
        ForeignKey(
            entity = SubCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["subCategoryId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["subCategoryId", "name"], unique = true)
    ])
data class BrandEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,
    val name: String,
    val subCategoryId: Int,
    val createdAt: Long,
    val updatedAt: Long
)