package com.litvy.litvysales.domain.model.promotion

data class PromotionTarget(
    val id: Int = 0,

    val promotionId: Int,

    val productId: Int? = null,
    val brandId: Int? = null,
    val categoryId: Int? = null,
    val batchId: Int? = null
)