package com.litvy.litvysales.domain.useCase.purchases.purchaseOrder

import com.litvy.litvysales.domain.interfaces.purchases.PurchaseOrderRepository
import com.litvy.litvysales.domain.model.purchases.PurchaseOrderItem
import kotlinx.coroutines.flow.Flow

class GetPurchaseOrderItemsUseCase(
    private val repository: PurchaseOrderRepository
) {

    suspend operator fun invoke(orderId: Int): Flow<List<PurchaseOrderItem>> {
        return repository.getItemsByOrderId(orderId)
    }
}
