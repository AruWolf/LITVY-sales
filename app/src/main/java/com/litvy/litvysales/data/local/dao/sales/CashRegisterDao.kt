package com.litvy.litvysales.data.local.dao.sales

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.sales.CashRegisterEntity
import kotlinx.coroutines.flow.Flow

interface CashRegisterDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cashRegister: CashRegisterEntity)

    @Query("SELECT * FROM cashRegister WHERE id = :id")
    suspend fun getById(id: Int): CashRegisterEntity?

    @Query("SELECT * FROM cashRegister WHERE location = :location ORDER BY id DESC")
    fun getByLocation(location: String): Flow<List<CashRegisterEntity?>>

    @Query("SELECT * FROM cashRegister WHERE active = 'true'")
    fun getActives(): Flow<List<CashRegisterEntity?>>

}