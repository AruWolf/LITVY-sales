package com.litvy.litvysales.domain.model.promotion

data class PromotionAggregate(

    val promotion: Promotion,

    val targets: List<PromotionTarget>,

    val requirements: List<PromotionProductRequirement>,

    val conditions: List<PromotionCondition>,

    val benefits: List<PromotionBenefit>
)