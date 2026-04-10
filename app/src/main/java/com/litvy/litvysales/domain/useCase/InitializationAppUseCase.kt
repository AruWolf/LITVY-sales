package com.litvy.litvysales.domain.useCase

import com.litvy.litvysales.data.local.entity.enums.CashSessionStatus
import com.litvy.litvysales.domain.interfaces.sales.CashRegisterRepository
import com.litvy.litvysales.domain.interfaces.sales.CashSessionRepository
import com.litvy.litvysales.domain.interfaces.user.RoleRepository
import com.litvy.litvysales.domain.interfaces.user.UserRepository
import com.litvy.litvysales.domain.model.sales.CashRegister
import com.litvy.litvysales.domain.model.sales.CashSession
import com.litvy.litvysales.domain.model.user.Role
import com.litvy.litvysales.domain.model.user.User

class InitializeAppUseCase(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val cashRegisterRepository: CashRegisterRepository,
    private val cashSessionRepository: CashSessionRepository
) {
    suspend operator fun invoke() {
        val now = System.currentTimeMillis()

        if (roleRepository.getById(DEFAULT_ROLE_ID) == null) {
            roleRepository.insert(
                Role(
                    id = DEFAULT_ROLE_ID,
                    name = "Admin"
                )
            )
        }

        if (userRepository.getById(DEFAULT_USER_ID) == null) {
            userRepository.create(
                User(
                    id = DEFAULT_USER_ID,
                    name = "Admin",
                    lastname = "System",
                    dni = "12345678",
                    telephoneNumber = "3511234567",
                    email = "admin@local",
                    birthDate = null,
                    address = "San Martin 123",
                    passwordHash = "Admin",
                    roleId = DEFAULT_ROLE_ID,
                    active = true,
                    createdAt = now,
                    updatedAt = now
                )
            )
        }

        if (cashRegisterRepository.getById(DEFAULT_CASH_REGISTER_ID) == null) {
            cashRegisterRepository.create(
                CashRegister(
                    id = DEFAULT_CASH_REGISTER_ID,
                    name = "Caja principal",
                    location = "Local principal",
                    active = true
                )
            )
        }

        if (cashSessionRepository.getById(DEFAULT_CASH_SESSION_ID) == null) {
            cashSessionRepository.create(
                CashSession(
                    id = DEFAULT_CASH_SESSION_ID,
                    cashRegisterId = DEFAULT_CASH_REGISTER_ID,
                    startedAt = now,
                    closedAt = null,
                    openingAmountInCents = 0L,
                    closingAmountInCents = null,
                    expectedAmountInCents = null,
                    differenceInCents = null,
                    status = CashSessionStatus.OPEN,
                    openedBy = DEFAULT_USER_ID,
                    closedBy = null
                )
            )
        }
    }

    private companion object {
        const val DEFAULT_ROLE_ID = 1
        const val DEFAULT_USER_ID = 1
        const val DEFAULT_CASH_REGISTER_ID = 1
        const val DEFAULT_CASH_SESSION_ID = 1
    }
}
