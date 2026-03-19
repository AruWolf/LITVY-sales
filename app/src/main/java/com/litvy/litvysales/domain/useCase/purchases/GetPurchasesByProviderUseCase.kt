package com.litvy.litvysales.domain.useCase.purchases

import com.litvy.litvysales.domain.filter.purchases.PurchaseFilter
import com.litvy.litvysales.domain.interfaces.purchases.PurchaseRepository
import com.litvy.litvysales.domain.model.purchases.Purchase
import kotlinx.coroutines.flow.Flow

class GetPurchasesByProviderUseCase(
    private val purchaseRepository: PurchaseRepository
) {

    suspend operator fun invoke(providerId: Int): Flow<List<Purchase>> {
        return purchaseRepository.getPurchases(
            PurchaseFilter(providerId = providerId)
        )
    }

}
