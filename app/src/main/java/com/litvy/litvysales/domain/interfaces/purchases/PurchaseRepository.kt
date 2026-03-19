package com.litvy.litvysales.domain.interfaces.purchases

import com.litvy.litvysales.domain.filter.purchases.PurchaseFilter
import com.litvy.litvysales.domain.model.purchases.Purchase
import com.litvy.litvysales.domain.model.purchases.PurchaseItem
import kotlinx.coroutines.flow.Flow

interface PurchaseRepository {

    suspend fun create(purchase: Purchase, purchaseItems: List<PurchaseItem> = emptyList()): Long

    suspend fun getById(id: Int): Purchase?

    suspend fun getItemsByPurchaseId(purchaseId: Int): Flow<List<PurchaseItem>>

    suspend fun getPurchases(filter: PurchaseFilter): Flow<List<Purchase>>
}
