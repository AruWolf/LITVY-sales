package com.litvy.litvysales.domain.useCase.sales.cashSession

class UpdateCashSessionDifferenceJustificationUseCase(
    private val getCashSessionByIdUseCase: GetCashSessionByIdUseCase,
    private val updateCashSessionUseCase: UpdateCashSessionUseCase
) {

    suspend operator fun invoke(sessionId: Int, justification: String?) {
        val session = getCashSessionByIdUseCase(sessionId)
            ?: throw IllegalStateException("No se encontró la sesión de caja.")

        if (session.closedAt == null) {
            throw IllegalStateException("La justificación solo puede editarse con la sesión cerrada.")
        }

        updateCashSessionUseCase(
            session.copy(differenceJustification = justification?.trim().takeUnless { it.isNullOrBlank() })
        )
    }
}
