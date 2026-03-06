package com.litvy.litvysales.data.repository.purchases

import com.litvy.litvysales.data.local.dao.purchases.PurchaseDao
import com.litvy.litvysales.domain.interfaces.purchases.PurchaseRepository
import com.litvy.litvysales.domain.model.purchases.Purchase
import com.litvy.litvysales.data.mapper.purchase.toEntity
import com.litvy.litvysales.data.mapper.purchase.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PurchaseRepositoryImpl(
    private val dao: PurchaseDao
) : PurchaseRepository {

    override suspend fun create(purchase: Purchase): Long {
        return dao.insert(purchase.toEntity())
    }

    override suspend fun getById(id: Int): Purchase? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun getByProvider(providerId: Int): Flow<List<Purchase>> {
        return dao.getByProvider(providerId).map { list -> list.map {it.toDomain()} }
    }
}