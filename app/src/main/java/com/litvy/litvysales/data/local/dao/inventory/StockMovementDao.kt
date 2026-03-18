package com.litvy.litvysales.data.local.dao.inventory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.litvy.litvysales.data.local.entity.inventory.StockMovementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockMovementDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun generateMovement(stockMovement: StockMovementEntity)

    @Query("SELECT * FROM stockMovement WHERE id = :stockMovementId")
    suspend fun getStockMovementById(stockMovementId: Int): StockMovementEntity?

    @Query("SELECT * FROM stockMovement WHERE productId = :productId")
    fun getStockMovementByProduct(productId: Int): Flow<List<StockMovementEntity>>

    @Query("SELECT * FROM stockMovement WHERE batchId = :batchId")
    fun getStockMovementByBatch(batchId: Int): Flow<List<StockMovementEntity>>

    @Query("SELECT * FROM stockMovement WHERE type = :type")
    fun getStockMovementByType(type: com.litvy.litvysales.domain.model.enums.StockMovementType): Flow<List<StockMovementEntity>>

    @Query("SELECT * FROM stockMovement WHERE createdAt = :createdAt")
    fun getStockMovementByDay(createdAt: Long): Flow<List<StockMovementEntity>>

    @Query("SELECT * FROM stockMovement WHERE referenceId = :referenceId")
    fun getStockMovementByReference(referenceId: Int): Flow<List<StockMovementEntity>>

    @Query("SELECT * FROM stockMovement WHERE createdBy = :userId")
    fun getStockMovementByUser(userId: Int): Flow<List<StockMovementEntity>>

    @Query("SELECT * FROM stockMovement")
    fun getAllStockMovement(): Flow<List<StockMovementEntity>>

    @RawQuery([StockMovementEntity::class])
    fun getByFilter(query: SupportSQLiteQuery): Flow<List<StockMovementEntity?>>
}