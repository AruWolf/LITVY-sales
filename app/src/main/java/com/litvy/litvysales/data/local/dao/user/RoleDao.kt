package com.litvy.litvysales.data.local.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.litvy.litvysales.data.local.entity.user.RoleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoleDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(role: RoleEntity): Long

    @Update
    suspend fun update(role: RoleEntity)

    @Query("SELECT * FROM role WHERE id = :roleId")
    suspend fun getById(roleId: Int): RoleEntity?

    @Query("SELECT * FROM role ORDER BY name ASC")
    fun getAll(): Flow<List<RoleEntity?>>

    @Query("SELECT COUNT(*) FROM role WHERE name = :name")
    suspend fun countByName(name: String): Int
}