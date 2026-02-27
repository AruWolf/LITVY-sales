package com.litvy.litvysales.data.local.entity

import androidx.room.*

@Entity(
    tableName = "cashMovement",
    foreignKeys = [
        ForeignKey(
            entity = CashSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["cashSessionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["createdBy"],
            onDelete = ForeignKey.RESTRICT
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