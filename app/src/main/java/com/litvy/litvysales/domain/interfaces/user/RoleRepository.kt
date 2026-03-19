package com.litvy.litvysales.domain.interfaces.user

import com.litvy.litvysales.domain.model.user.Role
import kotlinx.coroutines.flow.Flow

interface RoleRepository {

    suspend fun insert(role: Role): Long

    suspend fun update(role: Role)

    fun getAll(): Flow<List<Role?>>

    suspend fun getById(id: Int): Role?

    suspend fun exists(name: String): Boolean
}
