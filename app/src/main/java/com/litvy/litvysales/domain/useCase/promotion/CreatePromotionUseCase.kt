package com.litvy.litvysales.domain.useCase.promotion

import com.litvy.litvysales.domain.interfaces.promotion.PromotionRepository
import com.litvy.litvysales.domain.model.promotion.Promotion

class CreatePromotionUseCase(
        private val promotionRepository: PromotionRepository
) {
    suspend operator fun invoke(promotion: Promotion){


    }

}