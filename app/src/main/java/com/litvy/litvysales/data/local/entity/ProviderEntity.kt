package com.litvy.litvysales.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "provider")
data class ProviderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val cuit: String?,
    val telephoneNumber: String?,
    val address: String?,
    val email: String?,
    val visitingDays: List<String>
)