package com.litvy.litvysales.domain.interfaces.catalog

import com.litvy.litvysales.domain.model.catalog.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun insert(product: Product)
    suspend fun update(product: Product)
    suspend fun getActiveProducts(): Flow<List<Product>>
    suspend fun getByBrand(brandId: Int): Flow<List<Product>>
    suspend fun searchByName(query: String): Flow<List<Product>>
    //TODO: terminar de implementar
    //suspend fun getProductWithBrand(productId: Int)
    suspend fun countByBrand(brandId: Int): Int
    suspend fun deactivate(productId: Int, updatedAt: Long)
    //TODO: Terminar de implementar
    //suspend fun getProductFull(productId: Int):
}