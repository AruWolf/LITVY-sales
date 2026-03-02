package com.litvy.litvysales.data.local.entity.sales

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "customer",
    indices = [
        Index("cuit", unique = true)
    ]
)
data class CustomerEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val lastname: String?,

    val cuit: String?,

    val telephoneNumber: String?,
    val address: String?,
    val email: String?,

    val createdAt: Long
)