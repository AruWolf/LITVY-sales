package com.litvy.litvysales.domain.interfaces.purchases

import com.litvy.litvysales.domain.model.purchases.PurchaseItem

interface PurchaseItemRepository {

    suspend fun create(item: PurchaseItem): Long

    suspend fun getByPurchase(purchaseId: Int): List<PurchaseItem>
}