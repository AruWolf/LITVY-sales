package com.litvy.litvysales.domain.model.promotion

data class PromotionBenefit(
    val id: Int = 0,

    val promotionId: Int,

    val discountPercentage: Double? = null,
    val discountAmountInCents: Long? = null,
    val freeQuantity: Int? = null
)