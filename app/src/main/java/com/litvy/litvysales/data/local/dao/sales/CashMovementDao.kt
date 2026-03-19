package com.litvy.litvysales.data.local.dao.sales

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import com.litvy.litvysales.data.local.entity.sales.CashMovementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CashMovementDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun load(cashMovement: CashMovementEntity): Long

    @Query("SELECT * FROM cashMovement WHERE id = :cashMovementId")
    suspend fun getById(cashMovementId: Int): CashMovementEntity?

    @RawQuery([CashMovementEntity::class])
    fun getByFilter(query: SimpleSQLiteQuery): Flow<List<CashMovementEntity>>

}