package com.litvy.litvysales.data.local.dao.promotion

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.promotion.PromotionConditionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PromotionConditionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(condition: PromotionConditionEntity): Long

    @Query("SELECT * FROM promotion_condition WHERE promotionId = :promotionId")
    suspend fun getByPromotion(promotionId: Int): List<PromotionConditionEntity>

    @Query("DELETE FROM promotion_condition WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("UPDATE promotion_condition SET type = :type, value = :value WHERE id = :id")
    suspend fun update(
        id: Int,
        type: String,
        value: String
    )
}