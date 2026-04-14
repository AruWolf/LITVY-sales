package com.litvy.litvysales.domain.useCase.sales.cashSession

import com.litvy.litvysales.domain.interfaces.sales.CashSessionRepository
import com.litvy.litvysales.domain.model.enums.CashSessionStatus
import com.litvy.litvysales.domain.model.sales.CashSession

class CashSessionAlreadyOpenException(
    val sessionId: Int
) : IllegalStateException("Debe cerrar la sesión abierta antes de abrir una nueva.")

class OpenCashSessionUseCase(
    private val repository: CashSessionRepository
) {

    suspend operator fun invoke(
        registerId: Int,
        openedBy: Int,
        openingAmountInCents: Long,
        startedAt: Long = System.currentTimeMillis()
    ): CashSession {
        val existing = repository.getOpenSessionByRegister(registerId)
        if (existing != null) {
            throw CashSessionAlreadyOpenException(existing.id)
        }

        val session = CashSession(
            cashRegisterId = registerId,
            startedAt = startedAt,
            closedAt = null,
            openingAmountInCents = openingAmountInCents,
            closingAmountInCents = null,
            expectedAmountInCents = null,
            differenceInCents = null,
            differenceJustification = null,
            status = CashSessionStatus.OPEN,
            openedBy = openedBy,
            closedBy = null
        )

        val id = repository.create(session).toInt()
        return repository.getById(id) ?: session.copy(id = id)
    }
}
