package com.litvy.litvysales.domain.useCase.purchases.provider

import com.litvy.litvysales.domain.interfaces.purchases.ProviderRepository
import com.litvy.litvysales.domain.model.purchases.ProviderWithVisitDays
import kotlinx.coroutines.flow.Flow

class GetProvidersWithVisitDaysUseCase(
    private val repository: ProviderRepository
) {

    operator fun invoke(): Flow<List<ProviderWithVisitDays>> {
        return repository.getAll()
    }
}
