package com.litvy.litvysales.data.local.dao.promotion

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.promotion.PromotionTargetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PromotionTargetDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(target: PromotionTargetEntity): Long

    @Query("SELECT * FROM promotion_target WHERE promotionId = :promotionId")
    suspend fun getByPromotion(promotionId: Int): List<PromotionTargetEntity>

    @Query("DELETE FROM promotion_target WHERE id = :id")
    suspend fun delete(id: Int)
}