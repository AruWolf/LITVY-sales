package com.litvy.litvysales.domain.useCase.purchases.purchaseOrder

import com.litvy.litvysales.domain.interfaces.purchases.PurchaseOrderRepository
import com.litvy.litvysales.domain.model.purchases.PurchaseOrder

class GetPurchaseOrderByIdUseCase(
    private val repository: PurchaseOrderRepository
) {

    suspend operator fun invoke(orderId: Int): PurchaseOrder? {
        if (orderId <= 0) return null
        return repository.getById(orderId)
    }
}
