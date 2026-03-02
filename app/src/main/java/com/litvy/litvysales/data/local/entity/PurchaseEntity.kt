package com.litvy.litvysales.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "purchase",
    foreignKeys = [
        ForeignKey(
            entity = ProviderEntity::class,
            parentColumns = ["id"],
            childColumns = ["providerId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = InvoiceTypeEntity::class,
            parentColumns = ["id"],
            childColumns = ["invoiceTypeId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = PaymentMethodEntity::class,
            parentColumns = ["id"],
            childColumns = ["paymentMethodId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("providerId"), Index("invoiceTypeId"), Index("paymentMethodId")]
)
data class PurchaseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val providerId: Int,
    val invoiceTypeId: Int,
    val paymentMethodId: Int,
    val subtotalInCents: Long,
    val totalDiscountInCents: Long,
    val totalTaxInCents: Long,
    val totalInCents: Long,
    val createdAt: Long
)