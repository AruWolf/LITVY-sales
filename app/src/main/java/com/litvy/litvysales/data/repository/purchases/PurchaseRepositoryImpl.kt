package com.litvy.litvysales.data.repository.purchases

import com.litvy.litvysales.data.local.dao.purchases.PurchaseDao
import com.litvy.litvysales.data.local.dao.purchases.PurchaseItemDao
import com.litvy.litvysales.domain.interfaces.purchases.PurchaseRepository
import com.litvy.litvysales.domain.model.purchases.Purchase
import com.litvy.litvysales.data.mapper.purchase.toEntity
import com.litvy.litvysales.data.mapper.purchase.toDomain
import com.litvy.litvysales.domain.model.purchases.PurchaseItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PurchaseRepositoryImpl(
    private val dao: PurchaseDao,
    private val itemDao: PurchaseItemDao
) : PurchaseRepository {

    override suspend fun create(
        purchase: Purchase,
        purchaseItems: List<PurchaseItem>
    ) {

        val purchaseEntity = purchase.toEntity()

        val itemEntities = purchaseItems.map {
            it.toEntity()
        }

        dao.insertPurchaseWithItems(
            purchaseEntity,
            itemEntities
        )
    }

    override suspend fun getById(id: Int): Purchase? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun getByProvider(providerId: Int): Flow<List<Purchase>> {
        return dao.getByProvider(providerId).map { list -> list.map {it.toDomain()} }
    }

    override suspend fun getByInvoiceType(invoiceTypeId: Int): Flow<List<Purchase?>> {
        return dao.getByInvoiceType(invoiceTypeId).map { list -> list.map { it?.toDomain() } }
    }

    override suspend fun getByPaymentMethod(paymentMethodId: Int): Flow<List<Purchase?>> {
        return dao.getByPaymentMethod(paymentMethodId).map { list -> list.map { it?.toDomain() } }
    }

    override suspend fun getByCreatedDay(createdAt: Long): Flow<List<Purchase?>> {
        return dao.getByCreatedDay(createdAt).map { list -> list.map { it?.toDomain() } }
    }

    override suspend fun getItemsByPurchaseId(purchaseId: Int): Flow<List<PurchaseItem>> {
        return itemDao.getByPurchase(purchaseId).map { list -> list.map {it.toDomain()} }
    }

}