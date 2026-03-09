package com.litvy.litvysales.domain.model.promotion

import com.litvy.litvysales.domain.model.enums.PromotionBenefitType

data class PromotionBenefit(

    val id: Int = 0,

    val promotionId: Int,

    val type: PromotionBenefitType,

    val value: Long
)