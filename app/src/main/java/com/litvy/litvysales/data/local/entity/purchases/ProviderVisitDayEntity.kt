package com.litvy.litvysales.data.local.entity.purchases

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "provider_visit_day",
    primaryKeys = ["providerId", "dayOfWeek"],
    foreignKeys = [
        ForeignKey(
            entity = ProviderEntity::class,
            parentColumns = ["id"],
            childColumns = ["providerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("providerId")]
)
data class ProviderVisitDayEntity(
    val providerId: Int,
    val dayOfWeek: Int // 1=Lunes, 2=Martes, etc.
)