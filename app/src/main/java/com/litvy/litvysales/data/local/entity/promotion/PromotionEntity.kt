package com.litvy.litvysales.data.local.entity.promotion

import androidx.room.*

@Entity(
    tableName = "promotion",
    indices = [
        Index("active"),
        Index("startDate"),
        Index("endDate")
    ]
)
data class PromotionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val description: String?,

    val priority: Int = 0,
    val stackable: Boolean = true,
    val active: Boolean = true,
    val clearStock: Boolean = false,

    val startDate: Long?,
    val endDate: Long?,

    val createdAt: Long
)