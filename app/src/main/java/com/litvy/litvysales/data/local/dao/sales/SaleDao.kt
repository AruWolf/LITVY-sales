package com.litvy.litvysales.data.local.dao.sales

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.litvy.litvysales.data.local.entity.enums.SaleStatus
import com.litvy.litvysales.data.local.entity.sales.SaleEntity
import kotlinx.coroutines.flow.Flow

interface SaleDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(sale: SaleEntity): Long

    @Update
    suspend fun update(sale: SaleEntity)

    @Query("SELECT * FROM sale WHERE id = :id")
    suspend fun getById(id: Int): SaleEntity?

    @Query("SELECT * FROM sale WHERE cashSessionId = :sessionId ORDER BY createdAt ASC")
    fun getBySession(sessionId: Int): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sale WHERE sellerId = :userId ORDER BY createdAt DESC")
    fun getByUser(userId: Int): Flow<List<SaleEntity?>>

    @Query("SELECT * FROM sale WHERE customerId = :customerId ORDER BY createdAt DESC")
    fun getByCustomer(customerId: Int): Flow<List<SaleEntity?>>

    @Query("SELECT * FROM sale WHERE status = :status ORDER BY createdAt DESC")
    fun getAllCancelled(status: SaleStatus): Flow<List<SaleEntity?>>

    @Query("SELECT * FROM sale ORDER BY createdAt DESC")
    fun getAll(): Flow<List<SaleEntity?>>

    //TODO: Hacer consulta compleja por producto comprado
}