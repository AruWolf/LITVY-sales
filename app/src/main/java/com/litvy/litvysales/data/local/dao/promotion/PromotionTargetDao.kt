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
    suspend fun insert(promotionTarget: PromotionTargetEntity)

    @Query("SELECT * FROM promotion_target WHERE id = :id")
    suspend fun getById(id: Int): PromotionTargetEntity?

    @Query("SELECT * FROM promotion_target WHERE promotionId = :promotionId")
    suspend fun getByPromotion(promotionId: Int): PromotionTargetEntity

    @Query("SELECT * FROM promotion_target WHERE productId = :productId ORDER BY id DESC")
    fun getByProduct(productId: Int): Flow<List<PromotionTargetEntity?>>

    @Query("SELECT * FROM promotion_target WHERE brandId = :brandId ORDER BY id DESC")
    fun getByBrand(brandId: Int): Flow<List<PromotionTargetEntity?>>

    @Query("SELECT * FROM promotion_target WHERE categoryId = :categoryId ORDER BY id DESC")
    fun getByCategory(categoryId: Int): Flow<List<PromotionTargetEntity?>>

    @Query("SELECT * FROM promotion_target WHERE batchId = :batchId ORDER BY id DESC")
    fun getByBatch(batchId: Int): Flow<List<PromotionTargetEntity?>>

    @Query("SELECT * FROM promotion_target ORDER BY id DESC")
    fun getAll(): Flow<List<PromotionTargetEntity?>>
}