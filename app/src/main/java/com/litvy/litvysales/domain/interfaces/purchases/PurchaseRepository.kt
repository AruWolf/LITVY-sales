package com.litvy.litvysales.domain.interfaces.purchases

import com.litvy.litvysales.domain.model.purchases.Purchase
import kotlinx.coroutines.flow.Flow

interface PurchaseRepository {

    suspend fun create(purchase: Purchase): Long

    suspend fun getById(id: Int): Purchase?

    suspend fun getByProvider(providerId: Int): Flow<List<Purchase>>
}