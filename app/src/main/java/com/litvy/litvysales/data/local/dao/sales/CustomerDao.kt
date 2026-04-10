package com.litvy.litvysales.data.local.dao.sales

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import com.litvy.litvysales.data.local.entity.sales.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(customer: CustomerEntity): Long

    @Update
    suspend fun update(customer: CustomerEntity)

    @Query("SELECT * FROM customer WHERE id = :id")
    suspend fun getById(id: Int): CustomerEntity?

    @Query("SELECT * FROM customer ORDER BY lastname ASC")
    fun getAll(): Flow<List<CustomerEntity?>>

    @RawQuery([CustomerEntity::class])
    fun getByFilter(query: SimpleSQLiteQuery): Flow<List<CustomerEntity>>

}