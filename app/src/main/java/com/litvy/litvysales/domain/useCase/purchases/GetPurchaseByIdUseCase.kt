package com.litvy.litvysales.domain.useCase.purchases

import com.litvy.litvysales.domain.interfaces.purchases.PurchaseRepository
import com.litvy.litvysales.domain.model.purchases.Purchase

class GetPurchaseByIdUseCase(
    private val purchaseRepository: PurchaseRepository
) {

    suspend operator fun invoke(purchaseId: Int): Purchase? {
        return purchaseRepository.getById(purchaseId)
    }

}