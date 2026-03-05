package com.litvy.litvysales.data.local.dao.inventory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.inventory.StockBatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockBatchDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun loadBatch(batch: StockBatchEntity)

    @Query("SELECT * FROM stockBatch WHERE id = :batchId")
    suspend fun getFullBatch(batchId: Int): StockBatchEntity?

    @Query("SELECT * FROM stockBatch WHERE productId = :productid ORDER BY expirationDate ASC")
    fun getBatchByProduct(productid: Int): Flow<List<StockBatchEntity>>

}