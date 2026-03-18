package com.litvy.litvysales.data.repository.purchases

import com.litvy.litvysales.data.local.dao.purchases.ProviderDao
import com.litvy.litvysales.data.mapper.purchase.toDomain
import com.litvy.litvysales.data.mapper.purchase.toEntity
import com.litvy.litvysales.domain.interfaces.purchases.ProviderRepository
import com.litvy.litvysales.domain.model.purchases.ProviderWithVisitDays
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProviderRepositoryImpl(
    private val dao: ProviderDao
) : ProviderRepository {

    override suspend fun create(provider: ProviderWithVisitDays): Long {
        return dao.insertProviderWithDays(
            provider = provider.provider.toEntity(),
            visitDays = provider.visitDays
        )
    }

    override suspend fun update(provider: ProviderWithVisitDays) {
        dao.updateProviderWithDays(
            provider = provider.provider.toEntity(),
            visitDays = provider.visitDays
        )
    }

    override suspend fun getById(id: Int): ProviderWithVisitDays? {
        return dao.getById(id)?.toDomain()
    }

    override fun getByName(name: String): Flow<List<ProviderWithVisitDays>> {
        return dao.getByName(name).map { providers -> providers.map { it.toDomain() } }
    }

    override fun getAll(): Flow<List<ProviderWithVisitDays>> {
        return dao.getAll().map { providers -> providers.map { it.toDomain() } }
    }
}
