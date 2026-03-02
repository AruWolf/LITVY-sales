package com.litvy.litvysales.data.local.entity.catalog

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "category",
    indices = [Index("name", unique = true)])
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long
)