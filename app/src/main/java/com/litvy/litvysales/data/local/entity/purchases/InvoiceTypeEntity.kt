package com.litvy.litvysales.data.local.entity.purchases

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoice_type")
data class InvoiceTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val code: String, // A, B, C, REMITO, TICKET
    val description: String
)