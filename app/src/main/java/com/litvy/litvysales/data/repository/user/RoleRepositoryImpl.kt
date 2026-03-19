package com.litvy.litvysales.data.repository.user

import com.litvy.litvysales.data.local.dao.user.RoleDao
import com.litvy.litvysales.data.mapper.user.toDomain
import com.litvy.litvysales.data.mapper.user.toEntity
import com.litvy.litvysales.domain.interfaces.user.RoleRepository
import com.litvy.litvysales.domain.model.user.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoleRepositoryImpl(private val dao: RoleDao): RoleRepository {
    override suspend fun insert(role: Role): Long {
        return dao.insert(role.toEntity())
    }

    override suspend fun update(role: Role) {
        dao.update(role.toEntity())
    }

    override fun getAll(): Flow<List<Role?>> {
        return dao.getAll().map {
            list -> list.map { it?.toDomain()}
        }
    }

    override suspend fun getById(id: Int): Role? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun exists(name: String): Boolean {
        return dao.countByName(name) > 0
    }
}