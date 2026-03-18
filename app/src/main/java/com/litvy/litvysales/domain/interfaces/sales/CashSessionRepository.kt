package com.litvy.litvysales.domain.interfaces.sales

import com.litvy.litvysales.domain.filter.CashSessionFilter
import com.litvy.litvysales.domain.model.sales.CashSession
import kotlinx.coroutines.flow.Flow

interface CashSessionRepository {

    suspend fun create(session: CashSession): Long

    suspend fun update(session: CashSession)

    suspend fun getById(id: Int): CashSession?

    suspend fun getOpenSessionByRegister(registerId: Int): CashSession?

    fun getSessions(filter: CashSessionFilter): Flow<List<CashSession?>>
}