package com.litvy.litvysales.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.litvy.litvysales.data.local.entity.enums.CashSessionStatus

@Entity(
    tableName = "cashSession",
    foreignKeys = [
        ForeignKey(
            entity = CashRegisterEntity::class,
            parentColumns = ["id"],
            childColumns = ["cashRegisterId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["openedBy"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("cashRegisterId"), Index("openedBy")]
)
data class CashSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cashRegisterId: Int,
    val startedAt: Long,
    val closedAt: Long?,

    val openingAmountInCents: Long,
    val closingAmountInCents: Long?,
    val expectedAmountInCents: Long?,
    val differenceInCents: Long?,

    val status: CashSessionStatus,
    val openedBy: Int,
    val closedBy: Int?
)