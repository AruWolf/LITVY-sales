package com.litvy.litvysales.domain.interfaces.user

import com.litvy.litvysales.domain.model.user.Role

interface RoleRepository {

    suspend fun getAll(): List<Role>

    suspend fun getById(id: Int): Role?
}
