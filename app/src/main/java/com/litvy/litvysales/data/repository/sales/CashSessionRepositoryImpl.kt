package com.litvy.litvysales.data.repository.sales

import com.litvy.litvysales.data.local.dao.sales.CashSessionDao
import com.litvy.litvysales.data.local.query.CashSessionQueryBuilder
import com.litvy.litvysales.domain.filter.CashSessionFilter
import com.litvy.litvysales.domain.interfaces.sales.CashSessionRepository
import com.litvy.litvysales.domain.model.sales.CashSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.litvy.litvysales.data.mapper.sales.toDomain

class CashSessionRepositoryImpl(
    private val dao: CashSessionDao
) : CashSessionRepository {
    override suspend fun create(session: CashSession): Long {
        TODO("Not yet implemented")
    }

    override suspend fun update(session: CashSession) {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): CashSession? {
        TODO("Not yet implemented")
    }

    override suspend fun getOpenSessionByRegister(registerId: Int): CashSession? {
        TODO("Not yet implemented")
    }

    override fun getSessions(filter: CashSessionFilter) =
        dao.getByFilter(
            CashSessionQueryBuilder.build(filter)
        ).map { list ->
            list.map { it.toDomain() }
        }
}