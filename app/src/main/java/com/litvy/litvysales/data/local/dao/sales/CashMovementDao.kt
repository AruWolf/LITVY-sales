package com.litvy.litvysales.data.local.dao.sales

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.sales.CashMovementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CashMovementDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun load(cashMovement: CashMovementEntity)

    @Query("SELECT * FROM cashMovement WHERE id = :cashMovementId")
    suspend fun getById(cashMovementId: Int): CashMovementEntity?

    @Query("SELECT * FROM cashMovement WHERE cashSessionId = :cashSessionId ORDER BY createdAt DESC")
    fun getByCashSession(cashSessionId: Int): Flow<List<CashMovementEntity?>>

    @Query("SELECT * FROM cashMovement WHERE type = :type ORDER BY createdAt DESC")
    fun getByType(type: String): Flow<List<CashMovementEntity?>>

    @Query("SELECT * FROM cashMovement WHERE createdBy = :userId ORDER BY createdAt DESC")
    fun getByUser(userId: Int): Flow<List<CashMovementEntity?>>

}