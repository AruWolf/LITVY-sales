package com.litvy.litvysales.domain.useCase

import com.litvy.litvysales.domain.interfaces.user.RoleRepository
import com.litvy.litvysales.domain.interfaces.user.UserRepository
import com.litvy.litvysales.domain.model.user.Role
import com.litvy.litvysales.domain.model.user.User

class InitializeAppUseCase(
    private val repository: UserRepository,
    private val roleRepository: RoleRepository
) {
    suspend operator fun invoke() {
        val user = repository.getById(1)
        val role = roleRepository.getById(1)

        if (role == null){
            roleRepository.insert(
                Role(
                    id = 1,
                    name = "Admin"
                )
            )
        }

        if (user == null) {
            repository.create(
                User(
                    id = 1,
                    name = "Admin",
                    lastname = "System",
                    dni = "12345678",
                    telephoneNumber = "3511234567",
                    email = "admin@local",
                    birthDate = null,
                    address = "San Martin 123",
                    passwordHash = "Admin", // después lo mejorás
                    roleId = 1,
                    active = true,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }
}