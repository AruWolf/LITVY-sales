package com.litvy.litvysales.data.local.dao.promotion

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.promotion.PromotionBenefitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PromotionBenefitDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(promotionBenefit: PromotionBenefitEntity)

    @Query("SELECT * FROM promotion_benefit WHERE id = :id")
    suspend fun getById(id: Int): PromotionBenefitEntity?

    @Query("SELECT * FROM promotion_benefit WHERE promotionId = :promotionId")
    suspend fun getByPromotion(promotionId: Int): PromotionBenefitEntity

    @Query("SELECT * FROM promotion_benefit ORDER BY id DESC")
    fun getAll(): Flow<List<PromotionBenefitEntity?>>
}