package com.litvy.litvysales.domain.interfaces.purchases

import com.litvy.litvysales.domain.model.purchases.Provider

interface ProviderRepository {

    suspend fun create(provider: Provider): Long

    suspend fun update(provider: Provider)

    suspend fun getById(id: Int): Provider?

    suspend fun getAll(): List<Provider>
}