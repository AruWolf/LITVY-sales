package com.litvy.litvysales.domain.model.promotion

data class Promotion(
    val id: Int,

    val name: String,
    val description: String?,

    val priority: Int,
    val stackable: Boolean = true,
    val active: Boolean = true,
    val clearStock: Boolean = false,

    val startDate: Long?,
    val endDate: Long?,

    val createdAt: Long
)