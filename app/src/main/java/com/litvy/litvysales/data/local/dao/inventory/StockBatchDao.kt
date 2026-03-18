package com.litvy.litvysales.data.local.dao.inventory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.litvy.litvysales.data.local.entity.inventory.StockBatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockBatchDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun loadBatch(batch: StockBatchEntity)

    @Query("SELECT * FROM stockBatch WHERE id = :batchId")
    suspend fun getFullBatch(batchId: Int): StockBatchEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM stockBatch WHERE id = :batchId)")
    fun existsById(batchId: Int?): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM stockBatch WHERE productId = :productId)")
    fun belongsToProduct(batchId: Int, productId: Int): Boolean

    @RawQuery(observedEntities = [StockBatchEntity::class])
    fun getByFilter(query: SupportSQLiteQuery): Flow<List<StockBatchEntity>>
}