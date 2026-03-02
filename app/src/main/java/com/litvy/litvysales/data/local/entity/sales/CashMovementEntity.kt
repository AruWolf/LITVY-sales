package com.litvy.litvysales.data.local.entity.sales

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.litvy.litvysales.data.local.entity.user.UserEntity

@Entity(
    tableName = "cashMovement",
    foreignKeys = [
        ForeignKey(
            entity = CashSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["cashSessionId"],
            onDelete = ForeignKey.Companion.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["createdBy"],
            onDelete = ForeignKey.Companion.RESTRICT
        )
    ],
    indices = [
        Index("cashSessionId"),
        Index("createdBy"),
        Index("createdAt")
    ]
)
data class CashMovementEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val cashSessionId: Int,

    val type: String, // "IN" / "OUT"

    val amountInCents: Long,

    val reason: String?,

    val createdAt: Long,

    val createdBy: Int
)