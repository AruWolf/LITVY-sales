package com.litvy.litvysales.data.repository.promotion

import com.litvy.litvysales.data.local.dao.promotion.PromotionDao
import com.litvy.litvysales.data.mapper.promotion.toEntity
import com.litvy.litvysales.data.mapper.promotion.toDomain
import com.litvy.litvysales.domain.model.promotion.Promotion
import com.litvy.litvysales.domain.interfaces.promotion.PromotionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PromotionRepositoryImpl(
    private val dao: PromotionDao
) : PromotionRepository {

    override suspend fun createPromotion(promotion: Promotion): Long {
        return dao.insert(promotion.toEntity())
    }

    override suspend fun updatePromotion(promotion: Promotion) {
        dao.update(promotion.toEntity())
    }

    override suspend fun deletePromotion(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getActivePromotions(): Flow<List<Promotion>> {
        return dao.getAllActives().map { list -> list.map {it.toDomain()} }
    }

    override suspend fun getById(id: Int): Promotion? {
        return dao.getById(id)?.toDomain()
    }

    override fun getAll(): Flow<List<Promotion>> {
        TODO("Not yet implemented")
    }
}