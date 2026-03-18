package com.litvy.litvysales.data.local.dao.sales

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.sales.CashRegisterEntity
import kotlinx.coroutines.flow.Flow

interface CashRegisterDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cashRegister: CashRegisterEntity): Long

    @Query("UPDATE cashRegister SET active = NOT active WHERE id = :cashRegisterId")
    suspend fun toggleActive(cashRegisterId: Int)

    @Query("SELECT * FROM cashRegister WHERE id = :id")
    suspend fun getById(id: Int): CashRegisterEntity?

    @Query("SELECT * FROM cashRegister WHERE location = :location ORDER BY id DESC")
    fun getByLocation(location: String): Flow<List<CashRegisterEntity?>>

    @Query("SELECT * FROM cashRegister WHERE active")
    fun getActives(): Flow<List<CashRegisterEntity?>>

    @Query("SELECT * FROM cashRegister")
    fun getAll(): Flow<List<CashRegisterEntity?>>

    @Query("SELECT * FROM cashRegister WHERE name = :name")
    suspend fun getByName(name: String): CashRegisterEntity?

}