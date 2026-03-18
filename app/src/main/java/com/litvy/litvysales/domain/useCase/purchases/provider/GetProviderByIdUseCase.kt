package com.litvy.litvysales.domain.useCase.purchases.provider

import com.litvy.litvysales.domain.interfaces.purchases.ProviderRepository
import com.litvy.litvysales.domain.model.purchases.ProviderWithVisitDays

class GetProviderByIdUseCase(
    private val repository: ProviderRepository
) {

    suspend operator fun invoke(id: Int): ProviderWithVisitDays? {
        if (id <= 0) return null
        return repository.getById(id)
    }
}
