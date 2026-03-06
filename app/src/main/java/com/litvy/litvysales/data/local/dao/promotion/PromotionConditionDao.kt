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
    suspend fun insert(promotionCondition: PromotionConditionEntity)

    @Query("SELECT * FROM promotion_condition WHERE id = :id")
    suspend fun getById(id: Int): PromotionConditionEntity?

    @Query("SELECT * FROM promotion_condition WHERE promotionId = :promotionId")
    suspend fun getByPromotion(promotionId: Int): PromotionConditionEntity

    @Query("SELECT * FROM promotion_condition ORDER BY id DESC")
    fun getAll(): Flow<List<PromotionConditionEntity?>>

}