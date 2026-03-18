package com.litvy.litvysales.data.repository.purchases

import com.litvy.litvysales.data.local.dao.purchases.PurchaseOrderDao
import com.litvy.litvysales.data.local.dao.purchases.PurchaseOrderItemDao
import com.litvy.litvysales.data.mapper.purchase.toDomain
import com.litvy.litvysales.data.mapper.purchase.toEntity
import com.litvy.litvysales.domain.interfaces.purchases.PurchaseOrderRepository
import com.litvy.litvysales.domain.model.enums.PurchaseOrderStatus
import com.litvy.litvysales.domain.model.purchases.PurchaseOrder
import com.litvy.litvysales.domain.model.purchases.PurchaseOrderItem

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PurchaseOrderRepositoryImpl(
    private val dao: PurchaseOrderDao,
    private val itemDao: PurchaseOrderItemDao
) : PurchaseOrderRepository{

    override suspend fun create(purchaseOrder: PurchaseOrder, purchaseOrderItems: List<PurchaseOrderItem>) {
        val purchaseOrderEntity = purchaseOrder.toEntity()
        val itemEntities = purchaseOrderItems.map {
            it.toEntity()
        }

        return dao.insertWithItems(purchaseOrderEntity, itemEntities)
    }

    override suspend fun update(
        purchaseOrder: PurchaseOrder,
        purchaseOrderItems: List<PurchaseOrderItem>
    ) {

        val entity = purchaseOrder.toEntity()

        val items = purchaseOrderItems.map {
            it.toEntity()
        }

        dao.updateWithItems(entity, items)
    }

    override suspend fun getById(orderId: Int): PurchaseOrder? {
        return dao.getById(orderId)?.toDomain()
    }

    override suspend fun getAll(): Flow<List<PurchaseOrder?>> {
        return dao.getAll().map { list -> list.map { it?.toDomain() } }
    }

    override suspend fun getByProvider(providerId: Int): Flow<List<PurchaseOrder?>> {
        return dao.getByProvider(providerId).map { list -> list.map { it?.toDomain() } }
    }

    override suspend fun getByStatus(status: PurchaseOrderStatus): Flow<List<PurchaseOrder?>> {
        return dao.getByStatus(status.toEntity()).map { list -> list.map { it?.toDomain() }}
    }

    override suspend fun getByExpectedDeliveryDate(expectedDate: Long): Flow<List<PurchaseOrder?>> {
        return dao.getByExpectedDeliveryDate(expectedDate).map { list -> list.map { it?.toDomain() } }
    }

    override suspend fun getByCreationDate(creationDate: Long): Flow<List<PurchaseOrder?>> {
        return dao.getByCreationDate(creationDate).map { list -> list.map { it?.toDomain() }}
    }

    override suspend fun getByUser(userId: Int): Flow<List<PurchaseOrder?>> {
        return dao.getByUser(userId).map { list -> list.map { it?.toDomain() }}
    }

    override suspend fun getItemsByOrderId(orderId: Int): Flow<List<PurchaseOrderItem>>{
        return itemDao.getByOrderId(orderId).map { list -> list.map { it.toDomain() } }
    }
}