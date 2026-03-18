package com.litvy.litvysales.domain.interfaces.promotion

import com.litvy.litvysales.domain.filter.promotion.PromotionFilter
import com.litvy.litvysales.domain.model.promotion.*
import kotlinx.coroutines.flow.Flow

interface PromotionRepository {

    suspend fun createPromotion(promotion: Promotion): Long

    suspend fun updatePromotion(promotion: Promotion)

    suspend fun deletePromotion(id: Int)

    suspend fun getById(id: Int): Promotion?

    fun getAll(): Flow<List<Promotion>>

    fun getPromotions(filter: PromotionFilter): Flow<List<Promotion?>>

}