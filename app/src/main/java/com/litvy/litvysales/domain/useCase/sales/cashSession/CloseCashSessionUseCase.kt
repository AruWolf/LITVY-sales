package com.litvy.litvysales.domain.useCase.sales.cashSession

import com.litvy.litvysales.domain.model.enums.CashSessionStatus

class CloseCashSessionUseCase(
    private val getCashSessionDetailUseCase: GetCashSessionDetailUseCase,
    private val getCashSessionByIdUseCase: GetCashSessionByIdUseCase,
    private val updateCashSessionUseCase: UpdateCashSessionUseCase
) {

    suspend operator fun invoke(
        sessionId: Int,
        closingAmountInCents: Long,
        closedBy: Int,
        justification: String? = null,
        closedAt: Long = System.currentTimeMillis()
    ) {
        val detail = getCashSessionDetailUseCase(sessionId)
            ?: throw IllegalStateException("No se encontró la sesión de caja.")
        val session = getCashSessionByIdUseCase(sessionId)
            ?: throw IllegalStateException("No se encontró la sesión de caja.")

        if (session.status != CashSessionStatus.OPEN) {
            throw IllegalStateException("La sesión ya está cerrada.")
        }

        val difference = closingAmountInCents - detail.expectedAmountInCents

        updateCashSessionUseCase(
            session.copy(
                closedAt = closedAt,
                closingAmountInCents = closingAmountInCents,
                expectedAmountInCents = detail.expectedAmountInCents,
                differenceInCents = difference,
                differenceJustification = justification,
                status = CashSessionStatus.CLOSED,
                closedBy = closedBy
            )
        )
    }
}
