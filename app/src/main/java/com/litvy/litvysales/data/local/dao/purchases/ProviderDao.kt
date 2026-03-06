package com.litvy.litvysales.data.local.dao.purchases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.litvy.litvysales.data.local.entity.purchases.ProviderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProviderDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(provider: ProviderEntity)

    @Update
    suspend fun update(provider: ProviderEntity)

    @Query("SELECT * FROM provider WHERE id = :id")
    suspend fun getByid(id: Int): ProviderEntity?

    @Query("SELECT * FROM provider WHERE name = :name")
    fun getByName(name: String): Flow<List<ProviderEntity?>>

    @Query("SELECT * FROM provider ORDER BY id Desc")
    fun getAll(): Flow<List<ProviderEntity?>>
}