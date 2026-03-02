package com.litvy.litvysales.data.local.entity

import androidx.room.*

@Entity(tableName = "invoice_type")
data class InvoiceTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val code: String, // A, B, C, REMITO, TICKET
    val description: String
)