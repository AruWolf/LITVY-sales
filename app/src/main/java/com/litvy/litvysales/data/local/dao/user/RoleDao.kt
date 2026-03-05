package com.litvy.litvysales.data.local.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.litvy.litvysales.data.local.entity.user.RoleEntity

@Dao
interface RoleDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(role: RoleEntity)

    @Update
    suspend fun update(role: RoleEntity)

    @Query("SELECT * FROM role WHERE id = :roleId")
    suspend fun getById(roleId: Int): RoleEntity?

    @Query("SELECT * FROM role ORDER BY name ASC")
    suspend fun getAll(): List<RoleEntity>

    @Query("SELECT COUNT(*) FROM role WHERE name = :name")
    suspend fun countByName(name: String): Int
}