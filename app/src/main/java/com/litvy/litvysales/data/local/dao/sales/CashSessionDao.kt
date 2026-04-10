package com.litvy.litvysales.data.local.dao.sales

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.litvy.litvysales.data.local.entity.sales.CashSessionEntity
import com.litvy.litvysales.data.local.entity.enums.CashSessionStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface CashSessionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cashSession: CashSessionEntity)

    @Query("SELECT * FROM cashSession WHERE id = :id")
    suspend fun getById(id: Int): CashSessionEntity?

    @Query("SELECT * FROM cashSession ORDER BY closedAt DESC")
    fun getAll(): Flow<List<CashSessionEntity?>>

    @RawQuery(observedEntities = [CashSessionEntity::class])
    fun getByFilter(query: SupportSQLiteQuery): Flow<List<CashSessionEntity>>

    @Query("""
    SELECT * FROM cashSession
    WHERE cashRegisterId = :registerId
    AND closedAt IS NULL
    LIMIT 1
""")
    suspend fun getOpenSessionByRegister(registerId: Int): CashSessionEntity?

}

