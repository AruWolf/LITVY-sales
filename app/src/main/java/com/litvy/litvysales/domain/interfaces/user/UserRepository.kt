package com.litvy.litvysales.domain.interfaces.user

import com.litvy.litvysales.domain.filter.user.UserFilter
import com.litvy.litvysales.domain.model.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun create(user: User): Long

    suspend fun update(user: User)

    suspend fun getById(id: Int): User?


    suspend fun getAll(): Flow<List<User>>

    fun getUsers(filter: UserFilter): Flow<List<User?>>
}