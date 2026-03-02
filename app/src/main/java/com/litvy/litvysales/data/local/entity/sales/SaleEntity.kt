package com.litvy.litvysales.data.local.entity.sales

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.litvy.litvysales.data.local.entity.user.UserEntity
import com.litvy.litvysales.data.local.entity.enums.SaleStatus

@Entity(
    tableName = "sale",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["sellerId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = CashSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["cashSessionId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = CustomerEntity::class,
            parentColumns = ["id"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("sellerId"),
        Index("cashSessionId"),
        Index("createdAt")
    ]
)
data class SaleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val cashSessionId: Int,
    val sellerId: Int,
    val customerId: Int?,
    val totalDiscountInCents: Long,
    val totalInCents: Long,
    val status: SaleStatus,
    val cancellationReason: String?,
    val createdAt: Long
)