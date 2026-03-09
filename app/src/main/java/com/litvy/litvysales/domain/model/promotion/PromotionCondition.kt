package com.litvy.litvysales.domain.model.promotion

import com.litvy.litvysales.domain.model.enums.PromotionConditionType

data class PromotionCondition(

    val id: Int = 0,

    val promotionId: Int,

    val type: PromotionConditionType,

    val value: String
)
