package com.litvy.litvysales.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user",
    foreignKeys = [
        ForeignKey(
            entity = RoleEntity::class,
            parentColumns = ["id"],
            childColumns = ["roleId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("roleId"), Index("email", unique = true), Index("telephoneNumber", unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val telephoneNumber: String,
    val email: String?,
    val passwordHash: String,

    val roleId: Int,
    val active: Boolean,

    val createdAt: Long,
    val updatedAt: Long
)
