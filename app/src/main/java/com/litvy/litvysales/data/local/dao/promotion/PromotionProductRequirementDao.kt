package com.litvy.litvysales.data.local.dao.promotion

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.promotion.PromotionProductRequirementEntity

@Dao
interface PromotionProductRequirementDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(requirement: PromotionProductRequirementEntity): Long

    @Query("SELECT * FROM promotion_product_requirement WHERE promotionId = :promotionId")
    suspend fun getByPromotion(promotionId: Int): List<PromotionProductRequirementEntity>

    @Query("DELETE FROM promotion_product_requirement WHERE id = :id")
    suspend fun delete(id: Int)
}