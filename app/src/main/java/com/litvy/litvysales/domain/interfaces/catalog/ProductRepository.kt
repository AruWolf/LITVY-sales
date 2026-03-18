package com.litvy.litvysales.domain.interfaces.catalog

import com.litvy.litvysales.domain.model.catalog.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun insert(product: Product)
    suspend fun update(product: Product)
    fun getActiveProducts(): Flow<List<Product>>
    fun getByBrand(brandId: Int): Flow<List<Product>>
    fun searchByName(query: String): Flow<List<Product>>
    suspend fun getProductWithBrand(productId: Int)
    suspend fun countByBrand(brandId: Int): Int
    suspend fun deactivate(productId: Int, updatedAt: Long)
    suspend fun activate(productId: Int, updatedAt: Long)
    suspend fun getProductFull(productId: Int): Product
    suspend fun getById(productId: Int): Product?
    suspend fun existsByNameInBrandExcludingId(name: String, brandId: Int, productId: Int): Boolean
    suspend fun existsByNameInBrand(name: String, brandId: Int): Boolean
    suspend fun existsById(id: Int): Boolean
}