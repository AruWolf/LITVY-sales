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
    suspend fun insert(benefit: PromotionBenefitEntity): Long

    @Query("SELECT * FROM promotion_benefit WHERE promotionId = :promotionId")
    suspend fun getByPromotion(promotionId: Int): List<PromotionBenefitEntity>

    @Query("DELETE FROM promotion_benefit WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("UPDATE promotion_benefit SET type = :type, value = :value WHERE id = :id")
    suspend fun update(
        id: Int,
        type: String,
        value: Long
    )
}