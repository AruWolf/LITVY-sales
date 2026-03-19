package com.litvy.litvysales.domain.interfaces.purchases

import com.litvy.litvysales.domain.filter.purchases.PurchaseOrderFilter
import com.litvy.litvysales.domain.model.purchases.PurchaseOrder
import com.litvy.litvysales.domain.model.purchases.PurchaseOrderItem
import kotlinx.coroutines.flow.Flow

interface PurchaseOrderRepository {

    suspend fun create(purchaseOrder: PurchaseOrder, purchaseOrderItems: List<PurchaseOrderItem>)

    suspend fun update(purchaseOrder: PurchaseOrder, purchaseOrderItems: List<PurchaseOrderItem>)

    suspend fun getById(orderId: Int): PurchaseOrder?

    suspend fun getAll(): Flow<List<PurchaseOrder?>>

    suspend fun getItemsByOrderId(orderId: Int): Flow<List<PurchaseOrderItem>>

    fun getPurchaseOrders(filter: PurchaseOrderFilter): Flow<List<PurchaseOrder>>
}