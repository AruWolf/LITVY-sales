package com.litvy.litvysales.data.local.dao.sales

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.litvy.litvysales.data.local.entity.sales.CashSessionScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CashSessionScheduleDao {

    @Query("SELECT * FROM cashSessionSchedule ORDER BY dayOfWeek ASC, openMinuteOfDay ASC")
    fun getAll(): Flow<List<CashSessionScheduleEntity>>

    @Query("SELECT * FROM cashSessionSchedule WHERE id = :id")
    suspend fun getById(id: Int): CashSessionScheduleEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: CashSessionScheduleEntity): Long

    @Update
    suspend fun update(entity: CashSessionScheduleEntity)

    @Query("DELETE FROM cashSessionSchedule WHERE id = :id")
    suspend fun deleteById(id: Int)
}
