package com.litvy.litvysales.data.mapper.user

import com.litvy.litvysales.data.local.entity.user.*
import com.litvy.litvysales.domain.model.user.*

fun RoleEntity.toDomain() = Role(
    id,
    name
)

fun Role.toEntity() = RoleEntity(
    id,
    name
)

fun UserEntity.toDomain() = User(
    id,
    name,
    lastname,
    telephoneNumber,
    dni,
    birthDate,
    address,
    email,
    passwordHash,
    roleId,
    active,
    createdAt,
    updatedAt
)

fun User.toEntity() = UserEntity(
    id,
    name,
    lastname,
    telephoneNumber,
    dni,
    birthDate,
    address,
    email,
    passwordHash,
    roleId,
    active,
    createdAt,
    updatedAt
)
