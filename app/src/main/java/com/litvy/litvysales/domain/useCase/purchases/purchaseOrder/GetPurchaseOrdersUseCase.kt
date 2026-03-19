package com.litvy.litvysales.domain.useCase.purchases.purchaseOrder

import com.litvy.litvysales.domain.filter.purchases.PurchaseOrderFilter
import com.litvy.litvysales.domain.interfaces.purchases.PurchaseOrderRepository
import com.litvy.litvysales.domain.model.purchases.PurchaseOrder
import kotlinx.coroutines.flow.Flow

class GetPurchaseOrdersUseCase(
    private val repository: PurchaseOrderRepository
) {

    operator fun invoke(filter: PurchaseOrderFilter = PurchaseOrderFilter()): Flow<List<PurchaseOrder>> {
        return repository.getPurchaseOrders(filter)
    }
}
