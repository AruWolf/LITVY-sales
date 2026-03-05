package com.litvy.litvysales.data.local.dao.sales

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.sales.CashSessionEntity
import com.litvy.litvysales.data.local.entity.enums.CashSessionStatus
import kotlinx.coroutines.flow.Flow

interface CashSessionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cashSession: CashSessionEntity)

    @Query("SELECT * FROM cashSession WHERE id = :id")
    suspend fun getById(id: Int): CashSessionEntity?

    @Query("SELECT * FROM cashSession ORDER BY closedAt DESC")
    fun getAll(): Flow<List<CashSessionEntity?>>

    @Query("SELECT * FROM cashsession WHERE status = :status")
    fun getByStatus(status: CashSessionStatus): Flow<List<CashSessionEntity?>>

    @Query("SELECT * FROM cashSession WHERE openedBy = :userId ORDER BY startedAt DESC")
    fun getByOpeningUser(userId: Int): Flow<List<CashSessionEntity?>>

    @Query("SELECT * FROM cashSession WHERE closedBy = :userId ORDER BY closedAt DESC")
    fun getByClosingUser(userId: Int): Flow<List<CashSessionEntity?>>
}