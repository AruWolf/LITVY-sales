package com.litvy.litvysales.domain.useCase.purchases

import com.litvy.litvysales.domain.filter.purchases.PurchaseFilter
import com.litvy.litvysales.domain.interfaces.purchases.PurchaseRepository
import com.litvy.litvysales.domain.model.purchases.Purchase
import kotlinx.coroutines.flow.Flow

class GetPurchasesUseCase(
    private val purchaseRepository: PurchaseRepository
) {

    suspend operator fun invoke(filter: PurchaseFilter = PurchaseFilter()): Flow<List<Purchase>> {
        return purchaseRepository.getPurchases(filter)
    }
}
