package com.litvy.litvysales.domain.filter.promotion

data class PromotionFilter(
    val name: String? = null,
    val priority: Int? = null,
    val stackable: Boolean? = null,
    val active: Boolean? = null,
    val clearStock: Boolean? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val createdAt: Long? = null
)
