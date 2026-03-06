package com.litvy.litvysales.data.local.dao.promotion

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.litvy.litvysales.data.local.entity.promotion.PromotionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PromotionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(promotion: PromotionEntity): Long

    @Update
    suspend fun update(promotion: PromotionEntity)

    @Query("SELECT * FROM promotion WHERE id = :id")
    suspend fun getById(id: Int): PromotionEntity?

    @Query("SELECT * FROM promotion ORDER BY id DESC")
    fun getAll(): Flow<List<PromotionEntity>>

    @Query("SELECT * FROM promotion WHERE active = 'true'")
    fun getAllActives(): Flow<List<PromotionEntity>>


}