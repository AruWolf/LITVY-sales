package com.litvy.litvysales.domain.useCase.sales.cashSession

import com.litvy.litvysales.domain.filter.sales.CashMovementFilter
import com.litvy.litvysales.domain.filter.sales.SaleFilter
import com.litvy.litvysales.domain.interfaces.sales.CashMovementRepository
import com.litvy.litvysales.domain.interfaces.sales.CashSessionRepository
import com.litvy.litvysales.domain.interfaces.sales.SaleItemRepository
import com.litvy.litvysales.domain.interfaces.sales.SaleRepository
import com.litvy.litvysales.domain.model.sales.CashSessionDetail
import com.litvy.litvysales.domain.model.sales.CashSessionSaleSummary
import kotlinx.coroutines.flow.first

class GetCashSessionDetailUseCase(
    private val cashSessionRepository: CashSessionRepository,
    private val cashMovementRepository: CashMovementRepository,
    private val saleRepository: SaleRepository,
    private val saleItemRepository: SaleItemRepository
) {

    suspend operator fun invoke(sessionId: Int): CashSessionDetail? {
        val session = cashSessionRepository.getById(sessionId) ?: return null
        val movements = cashMovementRepository.getCashMovements(
            CashMovementFilter(cashSessionId = sessionId)
        ).first()
        val sales = saleRepository.getSales(SaleFilter(cashSessionId = sessionId)).first()

        val saleSummaries = sales.map { sale ->
            val items = saleItemRepository.getBySale(sale.id).first()
            CashSessionSaleSummary(
                saleId = sale.id,
                createdAt = sale.createdAt,
                itemCount = items.size,
                totalInCents = sale.totalInCents
            )
        }

        val totalSold = sales.sumOf { it.totalInCents }
        val manualDelta = movements.sumOf { movement ->
            when (movement.type.uppercase()) {
                "IN" -> movement.amountInCents
                "OUT" -> -movement.amountInCents
                else -> 0L
            }
        }
        val expectedAmount = session.openingAmountInCents + totalSold + manualDelta

        return CashSessionDetail(
            session = session,
            totalSoldInCents = totalSold,
            expectedAmountInCents = expectedAmount,
            sales = saleSummaries.sortedByDescending { it.createdAt },
            movements = movements.sortedByDescending { it.createdAt }
        )
    }
}
