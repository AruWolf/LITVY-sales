package com.litvy.litvysales.domain.model.sales

data class CashSessionSchedule(
    val id: Int = 0,
    val title: String?,
    val dayOfWeek: Int,
    val openMinuteOfDay: Int,
    val closeMinuteOfDay: Int,
    val active: Boolean
)
