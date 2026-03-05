package com.litvy.litvysales.domain.model.sales

data class SalePromotion(
    val id: Int = 0,

    val saleId: Int,
    val promotionId: Int,

    val discountAppliedInCents: Long
)