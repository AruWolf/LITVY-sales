package com.litvy.litvysales.domain.model.promotion

data class PromotionProductRequirement(

    val id: Int = 0,

    val promotionId: Int,

    val productId: Int,

    val requiredQuantity: Int
)