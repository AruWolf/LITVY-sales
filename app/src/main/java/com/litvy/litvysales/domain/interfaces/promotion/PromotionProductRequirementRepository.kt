package com.litvy.litvysales.domain.interfaces.promotion

import com.litvy.litvysales.domain.model.promotion.PromotionProductRequirement

interface PromotionProductRequirementRepository {

    suspend fun create(requirement: PromotionProductRequirement): Long

    suspend fun delete(id: Int)

    suspend fun getByPromotion(promotionId: Int): List<PromotionProductRequirement>
}