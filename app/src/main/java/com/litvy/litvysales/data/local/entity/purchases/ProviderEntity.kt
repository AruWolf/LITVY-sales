package com.litvy.litvysales.data.local.entity.purchases

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "provider")
data class ProviderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    val cuit: String?,
    val telephoneNumber: String?,
    val address: String?,
    val email: String?
)