package com.litvy.litvysales.data.local.entity.sales

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cashSessionSchedule")
data class CashSessionScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String?,
    val dayOfWeek: Int,
    val openMinuteOfDay: Int,
    val closeMinuteOfDay: Int,
    val active: Boolean
)
