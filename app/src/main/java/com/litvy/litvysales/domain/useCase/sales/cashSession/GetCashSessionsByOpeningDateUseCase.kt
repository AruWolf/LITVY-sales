package com.litvy.litvysales.domain.useCase.sales.cashSession

import com.litvy.litvysales.domain.filter.common.QuerySortDirection
import com.litvy.litvysales.domain.filter.sales.CashSessionFilter
import com.litvy.litvysales.domain.filter.sales.SaleFilter
import com.litvy.litvysales.domain.interfaces.sales.CashSessionRepository
import com.litvy.litvysales.domain.interfaces.sales.SaleRepository
import com.litvy.litvysales.domain.model.sales.CashSessionDefaults
import com.litvy.litvysales.domain.model.sales.CashSessionSummary
import kotlinx.coroutines.flow.first

class GetCashSessionsByOpeningDateUseCase(
    private val cashSessionRepository: CashSessionRepository,
    private val saleRepository: SaleRepository
) {

    suspend operator fun invoke(
        startOfDayMillis: Long,
        endOfDayMillis: Long,
        registerId: Int = CashSessionDefaults.DEFAULT_CASH_REGISTER_ID
    ): List<CashSessionSummary> {
        val sessions = cashSessionRepository.getSessions(
            CashSessionFilter(
                registerId = registerId,
                startDate = startOfDayMillis,
                endDate = endOfDayMillis,
                sortDirection = QuerySortDirection.DESC
            )
        ).first().filterNotNull()

        return sessions.map { session ->
            val sales = saleRepository.getSales(
                SaleFilter(cashSessionId = session.id)
            ).first()

            CashSessionSummary(
                sessionId = session.id,
                startedAt = session.startedAt,
                closedAt = session.closedAt,
                openedByLabel = "Usuario #${session.openedBy}",
                totalSoldInCents = sales.sumOf { it.totalInCents },
                status = session.status
            )
        }
    }
}
