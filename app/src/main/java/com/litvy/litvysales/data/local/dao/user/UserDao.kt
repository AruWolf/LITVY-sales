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
    suspend fun insert(user: UserEntity)

    @Update
    suspend fun update(user: UserEntity)

    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getById(userId: Int): UserEntity?

    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getByEmail(email: String): UserEntity?

    @Query("SELECT * FROM user WHERE active = 1")
    fun getActiveUsers(): Flow<List<UserEntity>>

    @Query("SELECT COUNT(*) FROM user WHERE email = :email")
    suspend fun countByEmail(email: String): Int
}