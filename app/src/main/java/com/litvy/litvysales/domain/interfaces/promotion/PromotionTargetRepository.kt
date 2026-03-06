package com.litvy.litvysales.domain.interfaces.promotion

import com.litvy.litvysales.domain.model.promotion.PromotionTarget

interface PromotionTargetRepository {

    suspend fun create(target: PromotionTarget): Long

    suspend fun delete(id: Int)

    suspend fun getByPromotion(promotionId: Int): List<PromotionTarget>

}