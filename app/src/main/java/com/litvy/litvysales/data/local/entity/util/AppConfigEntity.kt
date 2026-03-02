package com.litvy.litvysales.data.local.entity.util

import androidx.room.*

@Entity(tableName = "app_config")
data class AppConfigEntity(
    @PrimaryKey
    val key: String,
    val value: String,
    val updatedAt: Long
)