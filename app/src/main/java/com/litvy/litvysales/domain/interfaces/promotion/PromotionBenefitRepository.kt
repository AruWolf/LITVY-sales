package com.litvy.litvysales.domain.interfaces.promotion

import com.litvy.litvysales.domain.model.promotion.PromotionBenefit

interface PromotionBenefitRepository {

    suspend fun create(benefit: PromotionBenefit): Long

    suspend fun update(benefit: PromotionBenefit)

    suspend fun delete(id: Int)

    suspend fun getByPromotion(promotionId: Int): List<PromotionBenefit>

}