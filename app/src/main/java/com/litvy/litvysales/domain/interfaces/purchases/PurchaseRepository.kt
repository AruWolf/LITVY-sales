package com.litvy.litvysales.domain.interfaces.purchases

import com.litvy.litvysales.domain.model.purchases.InvoiceType
import com.litvy.litvysales.domain.model.purchases.Purchase
import com.litvy.litvysales.domain.model.purchases.PurchaseItem
import kotlinx.coroutines.flow.Flow

interface PurchaseRepository {

    suspend fun create(purchase: Purchase, purchaseItems: List<PurchaseItem>)

    suspend fun getById(id: Int): Purchase?

    suspend fun getByProvider(providerId: Int): Flow<List<Purchase?>>

    suspend fun getByInvoiceType(invoiceTypeId: Int): Flow<List<Purchase?>>

    suspend fun getByPaymentMethod(paymentMethodId: Int): Flow<List<Purchase?>>

    suspend fun getByCreatedDay(createdAt: Long): Flow<List<Purchase?>>

    suspend fun getItemsByPurchaseId(purchaseId: Int): Flow<List<PurchaseItem>>
}