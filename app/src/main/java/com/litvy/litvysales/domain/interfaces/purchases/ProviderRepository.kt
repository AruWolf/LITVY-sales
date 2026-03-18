package com.litvy.litvysales.domain.interfaces.purchases

import com.litvy.litvysales.domain.model.purchases.ProviderWithVisitDays
import kotlinx.coroutines.flow.Flow

interface ProviderRepository {

    suspend fun create(provider: ProviderWithVisitDays): Long

    suspend fun update(provider: ProviderWithVisitDays)

    suspend fun getById(id: Int): ProviderWithVisitDays?

    fun getByName(name: String): Flow<List<ProviderWithVisitDays>>

    fun getAll(): Flow<List<ProviderWithVisitDays>>
}
