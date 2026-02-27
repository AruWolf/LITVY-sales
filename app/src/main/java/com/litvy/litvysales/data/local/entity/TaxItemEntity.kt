package com.litvy.litvysales.data.local.entity

import androidx.room.*

@Entity(tableName = "taxItem")
data class TaxItemEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    val percentage: Double
)