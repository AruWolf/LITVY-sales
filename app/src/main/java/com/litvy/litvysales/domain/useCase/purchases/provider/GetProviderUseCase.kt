package com.litvy.litvysales.domain.useCase.purchases.provider

import com.litvy.litvysales.domain.filter.purchases.ProviderFilter
import com.litvy.litvysales.domain.interfaces.purchases.ProviderRepository
import com.litvy.litvysales.domain.model.purchases.Provider
import kotlinx.coroutines.flow.Flow

class GetProviderUseCase(
    private val repository: ProviderRepository
) {

    suspend operator fun invoke(filter: ProviderFilter): Flow<List<Provider>> {
        return repository.getProviders(filter)
    }
}