package com.litvy.litvysales.data.local.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.litvy.litvysales.data.local.entity.user.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity)

    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getById(userId: Int): UserEntity?

    @Query("SELECT * FROM user WHERE active = 1")
    fun getActiveUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user ORDER BY createdAt DESC")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user WHERE roleId = :roleId")
    fun getByRole(roleId: Int): Flow<List<UserEntity?>>

    @Query("SELECT * FROM user WHERE name = :name AND lastname = :lastName")
    fun getByName(name: String, lastName: String): Flow<UserEntity?>
}