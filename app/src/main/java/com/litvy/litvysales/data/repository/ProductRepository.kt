package com.litvy.litvysales.data.repository

import com.litvy.litvysales.data.local.dao.ProductDao
import com.litvy.litvysales.data.local.entity.catalog.ProductEntity
import com.litvy.litvysales.data.mapper.toEntity
import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository
import com.litvy.litvysales.domain.model.catalog.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepository(
    private val productDao: ProductDao
): ProductRepository {
    override suspend fun insert(product: Product) {
        productDao.insert(product.toEntity())
    }

    override suspend fun update(product: Product) {
        productDao.update(product.toEntity())
    }

    override suspend fun getActiveProducts(): Flow<List<Product>> {
        return productDao.getActiveProducts().let { getActiveProducts() }
    }

    override suspend fun getByBrand(brandId: Int): Flow<List<Product>> {
        return productDao.getByBrand(brandId).let { getByBrand(brandId) }
    }

    override suspend fun searchByName(query: String): Flow<List<Product>> {
        return productDao.searchByName(query).let { searchByName(query) }
    }

    override suspend fun countByBrand(brandId: Int): Int {
        return productDao.countByBrand(brandId)
    }

    override suspend fun deactivate(productId: Int, updatedAt: Long) {
        productDao.deactivate(productId, updatedAt)
    }

}