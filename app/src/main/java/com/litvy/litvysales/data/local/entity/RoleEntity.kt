package com.litvy.litvysales.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "role")
data class RoleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)