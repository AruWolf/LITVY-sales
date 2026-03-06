package com.litvy.litvysales.domain.model.promotion

data class PromotionCondition(
    val id: Int = 0,

    val promotionId: Int,

    val minQuantity: Int? = null,
    val maxQuantity: Int? = null,

    val requiredQuantity: Int? = null,

    val minSubtotalInCents: Long? = null
)
