package com.litvy.litvysales.domain.interfaces.promotion

import com.litvy.litvysales.domain.model.promotion.PromotionCondition

interface PromotionConditionRepository {

    suspend fun create(condition: PromotionCondition): Long

    suspend fun update(condition: PromotionCondition)

    suspend fun delete(id: Int)

    suspend fun getByPromotion(promotionId: Int): List<PromotionCondition>
}