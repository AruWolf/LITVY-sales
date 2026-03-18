package com.litvy.litvysales.domain.interfaces.purchases

import com.litvy.litvysales.domain.model.enums.PurchaseOrderStatus
import com.litvy.litvysales.domain.model.purchases.PurchaseOrder
import com.litvy.litvysales.domain.model.purchases.PurchaseOrderItem
import kotlinx.coroutines.flow.Flow

interface PurchaseOrderRepository {

    suspend fun create(purchaseOrder: PurchaseOrder, purchaseOrderItems: List<PurchaseOrderItem>)

    suspend fun update(purchaseOrder: PurchaseOrder, purchaseOrderItems: List<PurchaseOrderItem>)

    suspend fun getById(orderId: Int): PurchaseOrder?

    suspend fun getAll(): Flow<List<PurchaseOrder?>>

    suspend fun getByProvider(providerId: Int): Flow<List<PurchaseOrder?>>

    suspend fun getByStatus(status: PurchaseOrderStatus): Flow<List<PurchaseOrder?>>

    suspend fun getByExpectedDeliveryDate(expectedDate: Long): Flow<List<PurchaseOrder?>>

    suspend fun getByCreationDate(creationDate: Long): Flow<List<PurchaseOrder?>>

    suspend fun getByUser(userId: Int): Flow<List<PurchaseOrder?>>

    suspend fun getItemsByOrderId(orderId: Int): Flow<List<PurchaseOrderItem>>
}