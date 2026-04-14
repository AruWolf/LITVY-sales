package com.litvy.litvysales.domain.useCase.sales.cashSession

import com.litvy.litvysales.domain.interfaces.sales.CashMovementRepository
import com.litvy.litvysales.domain.model.sales.CashMovement

class RegisterCashMovementUseCase(
    private val repository: CashMovementRepository
) {

    suspend operator fun invoke(
        sessionId: Int,
        type: String,
        amountInCents: Long,
        reason: String,
        createdBy: Int,
        createdAt: Long = System.currentTimeMillis()
    ): Long {
        if (amountInCents <= 0L) {
            throw IllegalArgumentException("El monto debe ser mayor a cero.")
        }

        if (type.uppercase() !in setOf("IN", "OUT")) {
            throw IllegalArgumentException("Tipo de movimiento inválido.")
        }

        return repository.create(
            CashMovement(
                cashSessionId = sessionId,
                type = type.uppercase(),
                amountInCents = amountInCents,
                reason = reason.ifBlank { null },
                createdAt = createdAt,
                createdBy = createdBy
            )
        )
    }
}
