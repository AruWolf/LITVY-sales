package com.litvy.litvysales.domain.interfaces.purchases

import com.litvy.litvysales.domain.filter.purchases.ProviderFilter
import com.litvy.litvysales.domain.model.purchases.Provider
import com.litvy.litvysales.domain.model.purchases.ProviderByVisitDay
import com.litvy.litvysales.domain.model.purchases.ProviderWithVisitDays
import kotlinx.coroutines.flow.Flow

interface ProviderRepository {

    suspend fun create(provider: ProviderWithVisitDays): Long

    suspend fun update(provider: ProviderWithVisitDays)

    suspend fun getById(id: Int): ProviderWithVisitDays?

    fun getAll(): Flow<List<ProviderWithVisitDays>>

    fun getProviders(filter: ProviderFilter): Flow<List<Provider>>

    fun getByVisitDay(): Flow<List<ProviderByVisitDay>>
}
