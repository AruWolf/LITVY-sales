package com.litvy.litvysales.data.local.dao.sales

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import com.litvy.litvysales.data.local.entity.enums.SaleStatus
import com.litvy.litvysales.data.local.entity.sales.SaleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(sale: SaleEntity): Long

    @Update
    suspend fun update(sale: SaleEntity)

    @Query("SELECT * FROM sale WHERE id = :id")
    suspend fun getById(id: Int): SaleEntity?

    @Query("SELECT * FROM sale ORDER BY createdAt DESC")
    fun getAll(): Flow<List<SaleEntity?>>

    @RawQuery([SaleEntity::class])
    fun getByFilter(query: SimpleSQLiteQuery): Flow<List<SaleEntity>>

    //TODO: Hacer consulta compleja por producto comprado
}