package com.litvy.litvysales.data.local.dao.util

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.litvy.litvysales.data.local.entity.util.AppConfigEntity

@Dao
interface AppConfigDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(toEntity: AppConfigEntity)

    @Update
    suspend fun update()

    @Query("SELECT * FROM app_config WHERE 'key' = :id")
    suspend fun getById(id: String): AppConfigEntity
}