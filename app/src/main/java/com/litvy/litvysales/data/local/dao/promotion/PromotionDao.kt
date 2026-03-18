package com.litvy.litvysales.data.local.dao.promotion

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.litvy.litvysales.data.local.entity.promotion.PromotionEntity
import com.litvy.litvysales.domain.filter.promotion.PromotionFilter
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

    @RawQuery([PromotionEntity::class])
    fun getByFilter(query: SupportSQLiteQuery): Flow<List<PromotionEntity?>>
}