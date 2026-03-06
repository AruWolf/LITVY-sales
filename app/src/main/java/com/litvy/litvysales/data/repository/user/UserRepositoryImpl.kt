package com.litvy.litvysales.data.repository.user

import com.litvy.litvysales.data.local.dao.user.UserDao
import com.litvy.litvysales.data.mapper.user.toEntity
import com.litvy.litvysales.data.mapper.user.toDomain
import com.litvy.litvysales.domain.model.user.User
import com.litvy.litvysales.domain.interfaces.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val dao: UserDao
) : UserRepository {

    override suspend fun create(user: User): Long {
        return dao.insert(user.toEntity())
    }

    override suspend fun update(user: User) {
        dao.update(user.toEntity())
    }

    override suspend fun getById(id: Int): User? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun getAll(): Flow<List<User>> {
        return dao.getAll().map { list -> list.map {it.toDomain()}}
    }
}