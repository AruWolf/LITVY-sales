package com.litvy.litvysales.domain.interfaces.sales

import com.litvy.litvysales.domain.model.sales.CashSession

interface CashSessionRepository {

    suspend fun create(session: CashSession): Long

    suspend fun update(session: CashSession)

    suspend fun getById(id: Int): CashSession?

    suspend fun getOpenSessionByRegister(registerId: Int): CashSession?

    suspend fun getSessionsByPeriod(
        startDate: Long,
        endDate: Long
    ): List<CashSession>

}