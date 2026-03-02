package com.litvy.litvysales.data.local.entity.sales

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cashRegister")
data class CashRegisterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val location: String,
    val active: Boolean
)